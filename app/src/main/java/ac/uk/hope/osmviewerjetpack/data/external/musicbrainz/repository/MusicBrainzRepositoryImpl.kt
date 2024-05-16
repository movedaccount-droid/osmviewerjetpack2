package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.di.MusicBrainzLimiter
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

// repositories officially "own" our mapper functions and take the network/services
// to pass through them, responding with our final model

class MusicBrainzRepositoryImpl(
    private val artistDao: ArtistDao,
    private val areaDao: AreaDao,
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

    override fun getArtistReleaseGroups(mbid: String): Flow<List<ReleaseGroup>> {
        TODO("Not yet implemented")
    }

    private suspend fun getArtistFromNetwork(mbid: String) {
        withContext(dispatcher) {
            rateLimiter.startOperation()
            val artist = service.getArtist(mbid).toLocal()
            rateLimiter.endOperationAndLimit()
            upsertArtistWithRelations(artist)
        }
    }

    private suspend fun upsertArtistWithRelations(artist: ArtistWithRelationsLocal) {
        artistDao.upsert(artist.artist)
        artist.area?.let { areaDao.upsert(it) }
        artist.beginArea?.let { areaDao.upsert(it) }
    }

}