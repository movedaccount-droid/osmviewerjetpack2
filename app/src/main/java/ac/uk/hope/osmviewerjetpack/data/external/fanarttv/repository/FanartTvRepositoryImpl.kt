package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.AlbumImagesDao
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.ArtistImagesDao
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.FanartTvService
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.responses.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FanartTvRepositoryImpl(
    private val albumImagesDao: AlbumImagesDao,
    private val artistImagesDao: ArtistImagesDao,
    private val service: FanartTvService,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
): FanartTvRepository {

    private val rateLimiter = RateLimiter(1000)

    // get local first, and if not exists get from api
    // TODO: we should really really test this. the return if the dao doesn't find any result
    // is undefined: might throw exception, might null. who knows!!
    override suspend fun getArtistImages(
        mbid: String,
    ): Flow<ArtistImages> {
        return artistImagesDao.observe(mbid)
            .map { artistImages ->
                if (artistImages == null) {
                    updateArtistImagesFromNetwork(mbid)
                    ArtistImages()
                } else {
                    Log.d(TAG, "called with $mbid")
                    artistImages
                }
            }
    }

    // TODO: this kind of sucks: we're very likely to queue up a lot of these and then have the user
    // leave the page. they'll still be stuck resolving, though. we should store these jobs
    // somewhere (workmanager?) and then cancel them if they're no longer needed
    private suspend fun updateArtistImagesFromNetwork(mbid: String) {
        withContext(dispatcher) {
            // TODO: remove this, testing
            artistImagesDao.deleteAll()
            rateLimiter.await()
            val artistImages = service.getArtistImages(mbid).toLocal()
            artistImages.albums?.let { albumImagesDao.upsertAll(it) }
            artistImagesDao.upsert(artistImages.artist)
        }
    }
}