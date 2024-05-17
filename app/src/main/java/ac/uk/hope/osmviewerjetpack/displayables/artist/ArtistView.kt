package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.displayables.pieces.InfoCard
import ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list.album.ReleaseGroupLookupByArtist
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ArtistView(
    mbid: String,
    modifier: Modifier = Modifier,
    onReleaseSelected: (String) -> Unit = {},
) {

    val viewModel = hiltViewModel<ArtistViewViewModel,
            ArtistViewViewModel.ArtistViewViewModelFactory> { factory ->
        factory.create(mbid)
    }

    val expanded = rememberSaveable {
        mutableStateOf(false)
    }

    Column {
        viewModel.artist.value?.also { artist ->
            val table = mutableMapOf(
                "Tagged" to artist.sortedTags.joinToString(", ")
            )
            artist.activeText?.let { table["Active"] = it }
            artist.area?.name?.let { table["Area"] = it }
            artist.beginArea?.name?.let { table["Began"] = it }
            ReleaseGroupLookupByArtist(
                mbid = artist.mbid,
                onReleaseSelected = onReleaseSelected,
                header = {
                    InfoCard(
                        icon = viewModel.artistImages.value?.thumbnail,
                        name = artist.name,
                        desc = artist.shortDesc ?: "Artist",
                        table = table,
                        expanded = expanded.value,
                        onClick = { expanded.value = !expanded.value },
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    )
                }
            )
        } ?: run {
            // TODO: loading icon replacement card
            Text(text = "nothing...")
        }
    }

}