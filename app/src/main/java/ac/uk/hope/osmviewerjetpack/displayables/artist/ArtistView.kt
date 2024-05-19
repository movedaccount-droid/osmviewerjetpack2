package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.displayables.pieces.InfoCard
import ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list.album.ReleaseGroupLookupByArtist
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
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

    val unfollowButtonColors = ButtonDefaults.elevatedButtonColors()
    val followButtonColors = ButtonDefaults.elevatedButtonColors(
        containerColor = unfollowButtonColors.contentColor,
        contentColor = unfollowButtonColors.containerColor,
    )

    val buttonState = remember {
        derivedStateOf {
            if (viewModel.artistFollowed.value) {
                ButtonState (
                    icon = Icons.Default.Clear,
                    description = R.string.unfollow_artist_desc,
                    colors = unfollowButtonColors
                )
            } else {
                ButtonState (
                    icon = Icons.Default.Add,
                    description = R.string.follow_artist_desc,
                    colors = followButtonColors
                )
            }
        }
    }

    val buttonDescription = remember {
        derivedStateOf {
            if (viewModel.artistFollowed.value) {
            } else {
                R.string.unfollow_artist_desc
            }
        }
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
                        imageList = viewModel.artistImages.value?.backgrounds ?: listOf(),
                        imageWidth = 350.dp,
                        buttonContent = {
                            ElevatedButton(
                                onClick = { viewModel.toggleFollow() },
                                colors = buttonState.value.colors
                            ) {
                                Icon(
                                    imageVector = buttonState.value.icon,
                                    contentDescription = stringResource(buttonState.value.description),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            )
        } ?: run {
            // TODO: loading icon replacement card
            Text(text = "nothing...")
        }
    }
}

private data class ButtonState (
    val icon: ImageVector,
    val description: Int,
    val colors: ButtonColors
)