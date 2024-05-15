package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.AlbumImagesDao
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.ArtistImagesDao
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.FanartTvService
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.responses.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class FanartTvRepositoryImpl(
    private val albumImagesDao: AlbumImagesDao,
    private val artistImagesDao: ArtistImagesDao,
    private val service: FanartTvService,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
): FanartTvRepository {

    // implementing a rate limiter is awkward. who should have responsibility of ensuring fair use?
    // if we implement a queue, we assume everyone using this repo wants their operations queued
    // far in advance, but they might want to prioritize as availability arises.

    // if more than two people were using this repo at once, divvying work between them becomes
    // even more complicated. the ideal would probably be to submit tasks with some kind of tag,
    // and only allow queuing of one type of task at a time. but that's way overboard for us

    // instead we just assume users of this repo will avoid queuing too many tasks. not great
    private val rateLimiter = RateLimiter(1000)

    // get local first, and if not exists get from api
    override fun getArtistImages(
        mbid: String,
    ): Flow<ArtistImages> {
        return artistImagesDao.observe(mbid)
            .mapNotNull { artistImages ->
                if (artistImages == null) {
                    getArtistImagesFromNetwork(mbid)
                }
                artistImages
            }
    }

    private suspend fun getArtistImagesFromNetwork(mbid: String) {
        withContext(dispatcher) {
            rateLimiter.startOperation()
            try {
                val artistImages = service.getArtistImages(mbid).toLocal()
                artistImages.albums?.let { albumImagesDao.upsertAll(it) }
                artistImagesDao.upsert(artistImages.artist)
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    // fanarttv has no data for this artist, insert empty
                    artistImagesDao.upsert(ArtistImagesLocal(mbid))
                } else {
                    throw e
                }
            } finally {
                rateLimiter.endOperationAndLimit()
            }
        }
    }
}