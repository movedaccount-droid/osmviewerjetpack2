package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.toLocal
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException

// https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data#pagingsource

class MusicBrainzArtistSearchPagingSource
@AssistedInject constructor(
    private val service: MusicBrainzService,
    @Assisted private val query: String
): PagingSource<Int, Artist>() {

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    // TODO: include caching mechanism again
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {
        // TODO: this try-catch is taken straight from the codelab. we should check this and
        // make sure we cover everything we need [and don't cover anything we don't]
        try {
            val nextPageNumber = params.key ?: 0
            val loadSize = minOf(params.loadSize, 100)
            val offset = loadSize * nextPageNumber
            val response = service.searchArtists(query, loadSize, offset)
            val artists = response.toLocal().toExternal()
            return LoadResult.Page(
                data = artists,
                prevKey = null,
                nextKey = if (offset + artists.size + 1 == response.count) null else nextPageNumber + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}

// https://stackoverflow.com/questions/77013660/android-hilt-provide-object-with-dynamic-property

@AssistedFactory
interface MusicBrainzArtistSearchPagingSourceFactory {
    fun create(query: String): MusicBrainzArtistSearchPagingSource
}