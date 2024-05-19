package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.DetailedNotification
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
import ac.uk.hope.osmviewerjetpack.util.toDateString
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.di.MusicBrainzLimiter
import androidx.compose.ui.util.fastFilterNotNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.Calendar

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

    override fun getArtists(mbids: List<String>): Flow<List<Artist>> {
        var hasRunNetworkRequests = false
        return artistDao.observeAll(mbids)
            .distinctUntilChanged()
            .map {
                val retrievedMbids = it.mapNotNull { artist -> artist?.artist?.mbid }
                // only retrieve uncached artists once from network
                // TODO: obviously this fails if we hit a network error - we should throw an
                // exception to handle that in getArtistFromNetwork()
                if (!hasRunNetworkRequests) {
                    mbids.filter { mbid ->
                        !retrievedMbids.contains(mbid)
                    }.map { mbid ->
                        getArtistFromNetwork(mbid)
                    }
                    hasRunNetworkRequests = true
                }
                it.fastFilterNotNull().toExternal()
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

    override suspend fun updateFollowedCaches(): List<ReleaseGroup> {
        val followed = getFollowedArtists().first() // would have been nice to know this function
        val mutex = Mutex()
        val result = mutableListOf<ReleaseGroup>()
        val coroutines = followed.map {
            withContext(dispatcher) {
                launch {
                    val newReleases = updateArtistCache(it.mbid)
                    mutex.withLock {
                        result += newReleases
                    }
                }
            }
        }
        coroutines.joinAll()
        return result
    }

    private suspend fun updateArtistCache(artistMbid: String): List<ReleaseGroup> {
        return withContext(dispatcher) {
            // make sure we have the artist cached
            getArtist(artistMbid)
            // build query
            val follow = followDao.observe(artistMbid).first()!!
            val startDate = follow.started.toDateString()
            val endDate = Calendar.getInstance().toDateString()
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
                val notCached = releaseGroups.filterNot { cachedMbids.contains(it.id) }.toLocal()

                // cache and add notifications for the rest
                for (releaseGroup in notCached) {
                    releaseGroupDao.upsert(releaseGroup)
                    notificationDao.upsert(NotificationLocal(releaseGroup.mbid))
                }

                // since we were successful, update our sync count for this follow
                followDao.updateLastSyncCount(artistMbid, cacheOutdatedResponse.count)

                // return all new releases
                notCached.toExternal()
            } else {
                listOf()
            }
        }
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

    override fun getDetailedNotifications(): Flow<List<DetailedNotification>> {
        return notificationDao.observeAllWithDetailedReleaseGroups()
            .map { it.toExternal() }
    }

    override suspend fun addNotification(releaseGroupMbid: String) {
        notificationDao.upsert(NotificationLocal(releaseGroupMbid))
    }

    override suspend fun removeNotification(releaseGroupMbid: String) {
        notificationDao.delete(releaseGroupMbid)
    }

    override suspend fun prune() {
        artistDao.prune()
        areaDao.prune()
        releaseGroupDao.prune()
        releaseDao.prune()
    }

    private suspend fun getArtistFromNetwork(mbid: String): Job {
        return runBlocking {
            launch(dispatcher) {
                rateLimiter.startOperation()
                val artist = service.lookupArtist(mbid).toLocal()
                rateLimiter.endOperationAndLimit()
                upsertArtistWithRelations(artist)
            }
        }
    }

    private suspend fun upsertArtistWithRelations(artist: ArtistWithRelationsLocal): Job {
        return runBlocking {
            launch(dispatcher) {
                artistDao.upsert(artist.artist)
                artist.area?.let { areaDao.upsert(it) }
                artist.beginArea?.let { areaDao.upsert(it) }
            }
        }
    }

    private suspend fun getReleaseGroupFromNetwork(mbid: String): Job {
        TODO("currently we can only get releaseGroupMbid from a release group" +
                " in the first place, so this should never fire. holding off for now")
    }

    private suspend fun getReleaseFromNetwork(releaseGroupMbid: String): Job {
        return runBlocking {
            launch(dispatcher) {
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
}