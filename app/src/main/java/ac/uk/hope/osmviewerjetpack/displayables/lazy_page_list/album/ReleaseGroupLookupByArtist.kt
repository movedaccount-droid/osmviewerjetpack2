package ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list.album

import ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list.LazyPageList
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReleaseGroupLookupByArtist(
    mbid: String,
    onReleaseSelected: (mbid: String) -> Unit,
    header: (@Composable ColumnScope.() -> Unit)? = null
) {

    val viewModel = hiltViewModel<ReleaseGroupLookupByArtistViewModel,
            ReleaseGroupLookupByArtistViewModel.ReleaseGroupLookupByArtistViewModelFactory> { factory ->
        factory.create(mbid)
    }

    LazyPageList(
        pageFlow = viewModel.flow,
        getItemIcon = viewModel.getItemIcon,
        onItemSelected = onReleaseSelected,
        header = header
    )
}