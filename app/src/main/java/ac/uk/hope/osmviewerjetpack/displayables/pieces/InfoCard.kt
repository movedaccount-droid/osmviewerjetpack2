package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun InfoCard(
    icon: Uri?,
    name: String,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    desc: String? = null,
    table: Map<String, String> = mapOf(),
    imageList: List<Uri> = listOf(),
    imageWidth: Dp = 120.dp,
    buttonContent: (@Composable RowScope.() -> Unit)? = null
) {
    // TODO: lazy scrollable row of image cards
    // TODO: optional button for subscription
    Surface {
        Card (
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            onClick = onClick,
            modifier = modifier
        ) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                if (imageList.isNotEmpty()) {
                    ImageCarousel(
                        images = imageList,
                        description = "$name image",
                        imageWidth = imageWidth,
                        offset = 8.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    IconImage(
                        image = icon
                    )
                    Column (
                        Modifier.padding(8.dp)
                    ) {
                        Row {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                        desc?.let {
                            Text (
                                text = it,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
                if (expanded) {
                    Column (
                        modifier = Modifier.padding(8.dp)
                    ) {
                        table.map {
                            InfoLine(it.key, it.value)
                        }
                    }
                }
                buttonContent?.let {
                    Row (
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        it(this)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewInfoCard() {

    val expanded = rememberSaveable {
        mutableStateOf(false)
    }

    val uri = Uri.parse("https://thefader-res.cloudinary.com/private_images/w_760,c_limit,f_auto,q_auto:best/IMG_0301_brrdcn/song-you-need-lustsickpuppy-reclaims-her-iconography.jpg")

    OSMViewerJetpackTheme {
        InfoCard(
            icon = uri,
            name = "LustSickPuppy",
            desc = "Artist from Somewhere",
            table = mapOf(
                "Area" to "Somewhere",
                "Began" to "Somewhere Else",
                "Active" to "Yes (2023 - Present)",
                "Tagged" to "digital hardcore, hardcore, drum and bass"
            ),
            imageList = listOf(uri, uri, uri, uri),
            expanded = expanded.value,
            onClick = { expanded.value = !expanded.value },
            buttonContent = {
                ElevatedButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "some",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewInfoCardExpanded() {

    val expanded = rememberSaveable {
        mutableStateOf(true)
    }

    val uri = Uri.parse("https://thefader-res.cloudinary.com/private_images/w_760,c_limit,f_auto,q_auto:best/IMG_0301_brrdcn/song-you-need-lustsickpuppy-reclaims-her-iconography.jpg")

    OSMViewerJetpackTheme {
        InfoCard(
            icon = uri,
            name = "LustSickPuppy",
            desc = "Artist from Somewhere",
            table = mapOf(
                "Area" to "Somewhere",
                "Began" to "Somewhere Else",
                "Active" to "Yes (2023 - Present)",
                "Tagged" to "digital hardcore, hardcore, drum and bass"
            ),
            imageList = listOf(uri, uri, uri, uri),
            expanded = expanded.value,
            onClick = { expanded.value = !expanded.value },
            buttonContent = {
                ElevatedButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "some",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}