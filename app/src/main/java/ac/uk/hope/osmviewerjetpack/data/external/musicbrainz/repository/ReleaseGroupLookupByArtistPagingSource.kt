package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.BrowseReleaseGroupsResponse
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

// TODO: this could probably be genericized, lots of duplicate code
// from artistsearchpagingsource here
class ReleaseGroupLookupByArtistPagingSource
@AssistedInject constructor(
    @Assisted private val mbid: String,
    private val service: MusicBrainzService,
    private val releaseGroupDao: ReleaseGroupDao,
    @MusicBrainzLimiter private val rateLimiter: RateLimiter,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
): PagingSource<Int, ReleaseGroup>() {
    override fun getRefreshKey(state: PagingState<Int, ReleaseGroup>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReleaseGroup> {

        val nextPageNumber = params.key ?: 0
        val loadSize = minOf(params.loadSize, 100)
        val offset = loadSize * nextPageNumber

        val response: BrowseReleaseGroupsResponse
        try {
            rateLimiter.startOperation()
            response = service.browseReleaseGroupsByArtist(mbid, loadSize, offset)
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        } finally {
            rateLimiter.endOperationAndLimit()
        }

        val localResponse = response.toLocal()
        withContext(dispatcher) {
            releaseGroupDao.upsertAll(localResponse)
        }

        val releaseGroups = localResponse.toExternal()
        return LoadResult.Page(
            data = releaseGroups,
            prevKey = null,
            nextKey = if (offset + releaseGroups.size == response.releaseGroupCount) {
                null
            } else {
                nextPageNumber + 1
            }
        )
    }
}

@AssistedFactory
interface ReleaseGroupLookupByArtistPagingSourceFactory {
    fun create(mbid: String): ReleaseGroupLookupByArtistPagingSource
}