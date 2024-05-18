package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Release
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.FollowDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.NotificationDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowedLocalFactory
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.NotificationLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.di.MusicBrainzLimiter
import android.annotation.SuppressLint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

// repositories officially "own" our mapper functions and take the network/services
// to pass through them, responding with our final model

const val MAX_PAGE_LIMIT = 100

class MusicBrainzRepositoryImpl(
    private val artistDao: ArtistDao,
    private val areaDao: AreaDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseDao: ReleaseDao,
    private val followDao: FollowDao,
    private val notificationDao: NotificationDao,
    private val service: MusicBrainzService,
    @MusicBrainzLimiter private val rateLimiter: RateLimiter,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
): MusicBrainzRepository {

    // lookup artist by mbid
    override fun getArtist(mbid: String): Flow<Artist> {
        return artistDao.observe(mbid)
            .mapNotNull{ artist ->
                if (artist == null) {
                    getArtistFromNetwork(mbid)
                }
                artist?.toExternal()
            }
    }

    override fun getFollowedArtists(): Flow<List<Artist>> {
        // followed artists remain in cache, so no need to check network
        return artistDao.observeFollowed()
            .map(List<ArtistWithRelationsLocal>::toExternal)
    }

    override fun isFollowed(mbid: String): Flow<Boolean> {
        return followDao.observe(mbid)
            .map { it != null }
    }

    override suspend fun followArtist(mbid: String) {
        withContext(dispatcher) {
            followDao.upsert(FollowedLocalFactory.create(mbid))
            updateArtistCache(mbid)
        }
    }

    override suspend fun unfollowArtist(mbid: String) {
        withContext(dispatcher) {
            followDao.delete(mbid)
        }
    }

    override suspend fun updateFollowedCaches() {
        val followed = getFollowedArtists().first() // would have been nice to know this function
        for (artist in followed) {
            updateArtistCache(artist.mbid)
        }
    }

    private suspend fun updateArtistCache(artistMbid: String) {
        // make sure we have the artist cached
        getArtist(artistMbid)
        withContext(dispatcher) {
            // build query
            val follow = followDao.observe(artistMbid).first()!!
            val startDate = follow.started.timeInMillis.toMusicBrainzTimestamp()
            val endDate = System.currentTimeMillis().toMusicBrainzTimestamp()
            val followedReleaseGroupQuery = "arid:$artistMbid AND date:[$startDate TO $endDate]"

            // check if cache outdated
            val cacheOutdatedResponse = service.searchReleaseGroups(
                query = followedReleaseGroupQuery,
                limit = 1,
                offset = 0
            )

            if (cacheOutdatedResponse.count != follow.lastSyncCount) {
                // update cache. paging 3 seems overkill for this
                // iterate the entire set of releases since subscription
                var offset = 0
                val releaseGroups = mutableListOf<ReleaseGroupNetwork>()
                do {
                    val response = service.searchReleaseGroups(
                        query = followedReleaseGroupQuery,
                        limit = MAX_PAGE_LIMIT,
                        offset = offset
                    )
                    releaseGroups += response.releaseGroups
                    offset += MAX_PAGE_LIMIT
                } while (response.releaseGroups.size == MAX_PAGE_LIMIT)

                // filter anything we already have cached
                // TODO: this literally observes the objects instead of just checking them
                // for presence, which is expensive. this should be a presence check
                val cachedMbids = releaseGroupDao
                    .observeAll(releaseGroups.map { it.id })
                    .first()
                    .map { it.mbid }
                val notCached = releaseGroups.filterNot { cachedMbids.contains(it.id) }

                // cache and add notifications for the rest
                for (releaseGroup in notCached) {
                    val localReleaseGroup = releaseGroup.toLocal()
                    releaseGroupDao.upsert(localReleaseGroup)
                    notificationDao.upsert(NotificationLocal(localReleaseGroup.mbid))
                }

                // since we were successful, update our sync count for this follow
                followDao.updateLastSyncCount(artistMbid, cacheOutdatedResponse.count)
            }
        }
    }

    private suspend fun getArtistFromNetwork(mbid: String) {
        withContext(dispatcher) {
            rateLimiter.startOperation()
            val artist = service.lookupArtist(mbid).toLocal()
            rateLimiter.endOperationAndLimit()
            upsertArtistWithRelations(artist)
        }
    }

    private suspend fun upsertArtistWithRelations(artist: ArtistWithRelationsLocal) {
        artistDao.upsert(artist.artist)
        artist.area?.let { areaDao.upsert(it) }
        artist.beginArea?.let { areaDao.upsert(it) }
    }


    override fun getReleaseWithReleaseGroup(
        releaseGroupMbid: String
    ): Flow<Pair<ReleaseGroup, Release>> {
        return releaseGroupDao.observeWithRelationships(releaseGroupMbid)
            .mapNotNull{ releaseMap ->
                if (releaseMap.isEmpty()) {
                    getReleaseGroupFromNetwork(releaseGroupMbid)
                    null
                } else {
                    val releasePair = releaseMap.entries.first()
                    if (releasePair.value == null) {
                        getReleaseFromNetwork(releaseGroupMbid)
                        null
                    } else {
                        Pair(
                            releasePair.key.toExternal(),
                            releasePair.value!!.toExternal()
                        )
                    }
                }
            }
    }

    private suspend fun getReleaseGroupFromNetwork(mbid: String) {
        TODO("currently we can only get releaseGroupMbid from a release group" +
                " in the first place, so this should never fire. holding off for now")
    }

    private suspend fun getReleaseFromNetwork(releaseGroupMbid: String) {
        withContext(dispatcher) {
            rateLimiter.startOperation()
            val release = service.browseReleases(
                mbid = releaseGroupMbid,
                limit = 1,
                offset = 0
            ).releases[0]
            rateLimiter.endOperationAndLimit()
            releaseDao.upsert(release.toLocal(releaseGroupMbid))
        }
    }

}

@SuppressLint("SimpleDateFormat")
private fun Long.toMusicBrainzTimestamp() {
    SimpleDateFormat("yyyy-MM-dd").format(Date(this))
}