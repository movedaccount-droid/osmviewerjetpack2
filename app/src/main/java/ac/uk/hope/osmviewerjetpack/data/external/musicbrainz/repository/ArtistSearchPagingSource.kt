package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.ArtistSearchResponse
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.toLocal
import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.di.MusicBrainzLimiter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

// https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data#pagingsource

class ArtistSearchPagingSource
@AssistedInject constructor(
    private val service: MusicBrainzService,
    private val artistDao: ArtistDao,
    private val areaDao: AreaDao,
    @MusicBrainzLimiter private val rateLimiter: RateLimiter,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @Assisted private val query: String
): PagingSource<Int, Artist>() {

    // this seems to be boilerplate
    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    // TODO: include caching mechanism again
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {

        val nextPageNumber = params.key ?: 0
        val loadSize = minOf(params.loadSize, 100)
        val offset = loadSize * nextPageNumber

        // TODO: this try-catch is taken straight from the codelab. we should check this and
        // make sure we cover everything we need [and don't cover anything we don't]
        val response: ArtistSearchResponse
        try {
            rateLimiter.startOperation()
            response = service.searchArtists(query, loadSize, offset)
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        } finally {
            rateLimiter.endOperationAndLimit()
        }

        // cache contained artists, since we have them
        // caching the scores is probably not worth it given the regularity such a wide-spread
        // search will invalidate with
        val localResponse = response.toLocal()
        withContext(dispatcher) {
            upsertArtistsWithRelations(localResponse)
        }

        val artists = localResponse.toExternal()
        return LoadResult.Page(
            data = artists,
            prevKey = null,
            nextKey = if (offset + artists.size + 1 == response.count) null else nextPageNumber + 1
        )
    }

    private suspend fun upsertArtistsWithRelations(artists: List<ArtistWithRelationsLocal>) {
        artistDao.upsertAll(artists.map { it.artist })
        areaDao.upsertAll(artists.mapNotNull { it.area })
        areaDao.upsertAll(artists.mapNotNull { it.beginArea })
    }
}

// https://stackoverflow.com/questions/77013660/android-hilt-provide-object-with-dynamic-property

@AssistedFactory
interface ArtistSearchPagingSourceFactory {
    fun create(query: String): ArtistSearchPagingSource
}