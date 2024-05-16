package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.displayables.search.album.ReleaseGroupLookupByArtist
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ArtistView(
    mbid: String,
    modifier: Modifier = Modifier
) {

    val viewModel: ArtistViewViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.getArtist(mbid)
    }

    Column (
        modifier.padding(8.dp)
    ) {
        viewModel.artist.value?.also { artist ->
            // TODO: lazy scrollable row of image cards
            ReleaseGroupLookupByArtist(
                mbid = artist.mbid,
                onAlbumSelected = {},
                header = {
                    ArtistViewDetail(
                        icon = viewModel.artistImages.value?.thumbnail,
                        name = artist.name,
                        desc = artist.shortDesc ?: "Artist",
                        area = artist.area?.name,
                        beginArea = artist.beginArea?.name,
                        active = artist.activeText,
                        tags = artist.sortedTags.joinToString(", ")
                    )
                }
            )
        } ?: run {
            // TODO: loading icon in center of screen
            Text(text = "nothing...")
        }
    }

}