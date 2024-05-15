package ac.uk.hope.osmviewerjetpack.displayables.search.album

import ac.uk.hope.osmviewerjetpack.displayables.search.Search
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AlbumSearch(
    artistMbid: String,
    onAlbumSelected: (mbid: String) -> Unit
) {
    val viewModel: AlbumSearchViewModel = hiltViewModel()
    Search(
        searcher = viewModel.getSearcher(artistMbid),
        onItemSelected = onAlbumSelected
    )
}