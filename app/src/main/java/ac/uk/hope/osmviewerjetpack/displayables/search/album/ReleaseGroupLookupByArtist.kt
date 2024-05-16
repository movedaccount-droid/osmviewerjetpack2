package ac.uk.hope.osmviewerjetpack.displayables.search.album

import ac.uk.hope.osmviewerjetpack.displayables.search.Search
import ac.uk.hope.osmviewerjetpack.displayables.search.artist.ArtistSearchViewModel
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReleaseGroupLookupByArtist(
    mbid: String,
    onAlbumSelected: (mbid: String) -> Unit,
    header: (@Composable ColumnScope.() -> Unit)? = null
) {

    val viewModel = hiltViewModel<ReleaseGroupLookupByArtistViewModel,
            ReleaseGroupLookupByArtistViewModel.ReleaseGroupLookupByArtistViewModelFactory> { factory ->
        factory.create(mbid)
    }

    Search(
        resultFlow = viewModel.flow,
        getItemIcon = viewModel.getItemIcon,
        onItemSelected = onAlbumSelected,
        header = header
    )
}