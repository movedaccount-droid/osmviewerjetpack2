package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException

// repositories officially "own" our mapper functions and take the network/services
// to pass through them, responding with our final model

class MusicBrainzRepositoryImpl(
    private val artistDao: ArtistDao,
    private val areaDao: AreaDao,
    private val musicBrainzService: MusicBrainzService,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
): MusicBrainzRepository {

    private val rateLimiter = RateLimiter(1000)

    override fun searchArtistsName(
        query: String,
        limit: Int,
        offset: Int
    ): Flow<List<Artist>> {
        // we always have to make an external search to retrieve the scores. though we could cache
        // searches too... still, no current need for offline-first approach
        return flow {
            rateLimiter.startOperation()
            val artists = musicBrainzService.searchArtists(
                "artist:$query",
                limit,
                offset
            ).artists.toLocal()
            rateLimiter.endOperationAndLimit()
            artistDao.upsertAll(artists.map { it.artist })
            areaDao.upsertAll(artists.mapNotNull { it.area })
            areaDao.upsertAll(artists.mapNotNull { it.beginArea })
            emit(artists.toExternal())
        }
    }
}