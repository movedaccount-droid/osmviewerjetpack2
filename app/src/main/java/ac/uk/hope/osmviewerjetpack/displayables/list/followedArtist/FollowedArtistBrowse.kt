package ac.uk.hope.osmviewerjetpack.displayables.list.followedArtist

import ac.uk.hope.osmviewerjetpack.displayables.list.ItemList
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FollowedArtistBrowse(
    onArtistSelected: (mbid: String) -> Unit
) {
    val viewModel: FollowedArtistBrowseViewModel = hiltViewModel()

    ItemList(
        items = viewModel.flow,
        getItemIcon = viewModel.getItemIcon,
        onItemSelected = onArtistSelected
    )
}