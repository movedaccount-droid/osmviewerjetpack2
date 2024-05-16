package ac.uk.hope.osmviewerjetpack.displayables.search.album

import ac.uk.hope.osmviewerjetpack.displayables.search.Search
import ac.uk.hope.osmviewerjetpack.displayables.search.SearchResult
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig

@Composable
fun AlbumSearch(
    artistMbid: String,
    onAlbumSelected: (mbid: String) -> Unit
) {
    val viewModel: AlbumSearchViewModel = hiltViewModel()

//    Search(
//        searcher = viewModel.getSearcher(artistMbid),
//        onItemSelected = onAlbumSelected
//    )
}