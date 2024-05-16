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

    val viewModel = hiltViewModel<ArtistSearchViewModel,
            ArtistSearchViewModel.ArtistSearchViewModelFactory> { factory ->
        factory.create(query)
    }

    Search(
        resultFlow = viewModel.flow,
        getItemIcon = viewModel.getItemIcon,
        onItemSelected = onArtistSelected
    )
}