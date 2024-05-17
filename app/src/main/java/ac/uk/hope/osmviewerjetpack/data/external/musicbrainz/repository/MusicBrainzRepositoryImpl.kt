package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Release
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.FollowedDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupWithReleaseLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.di.MusicBrainzLimiter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

// repositories officially "own" our mapper functions and take the network/services
// to pass through them, responding with our final model

class MusicBrainzRepositoryImpl(
    private val artistDao: ArtistDao,
    private val areaDao: AreaDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseDao: ReleaseDao,
    private val followedDao: FollowedDao,
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

    override fun isArtistFollowed(mbid: String): Flow<Boolean> {
        return followedDao.getFollowed(mbid)
            .map { it != null }
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
        return releaseGroupDao.observeWithRelease(releaseGroupMbid)
            .mapNotNull{ releasePair ->
                if (releasePair == null) {
                    getReleaseGroupFromNetwork(releaseGroupMbid)
                    null
                } else if (releasePair.release == null) {
                    getReleaseFromNetwork(releaseGroupMbid)
                    null
                } else {
                    Pair(
                        releasePair.releaseGroup.toExternal(),
                        releasePair.release.toExternal()
                    )
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
            val release = service.browseReleasesByReleaseGroup(
                mbid = releaseGroupMbid,
                limit = 1,
                offset = 0
            ).releases[0]
            rateLimiter.endOperationAndLimit()
            releaseDao.upsert(release.toLocal(releaseGroupMbid))
        }
    }

}