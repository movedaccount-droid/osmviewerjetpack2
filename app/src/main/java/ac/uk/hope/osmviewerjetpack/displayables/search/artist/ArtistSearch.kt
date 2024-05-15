package ac.uk.hope.osmviewerjetpack.displayables.search.artist

import ac.uk.hope.osmviewerjetpack.displayables.search.Search
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

// retrieves search results for a generic artist query

@Composable
fun ArtistSearch(
    query: String,
    onArtistSelected: (mbid: String) -> Unit
) {
    val viewModel: ArtistSearchViewModel = hiltViewModel()
    Search(
        searcher = viewModel.getSearcher(query),
        onItemSelected = onArtistSelected
    )
}