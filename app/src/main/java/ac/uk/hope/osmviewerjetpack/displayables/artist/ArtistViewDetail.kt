package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.displayables.pieces.IconImage
import ac.uk.hope.osmviewerjetpack.displayables.pieces.InfoLine
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ArtistViewDetail(
    icon: Uri?,
    name: String,
    desc: String = "Artist",
    area: String?,
    beginArea: String?,
    active: String?,
    tags: String?,
) {
    Surface {
        Column {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
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
                    Text (
                        text = desc,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            area?.let{ InfoLine("Area", area) }
            beginArea?.let{ InfoLine("Began", beginArea) }
            active?.let{ InfoLine("Active", active) }
            tags?.let{ InfoLine("Tagged", tags) }
        }
    }
}

@Preview
@Composable
private fun PreviewArtistViewDetail() {
    OSMViewerJetpackTheme {
        ArtistViewDetail(
            icon = Uri.parse("https://thefader-res.cloudinary.com/private_images/w_760,c_limit,f_auto,q_auto:best/IMG_0301_brrdcn/song-you-need-lustsickpuppy-reclaims-her-iconography.jpg"),
            name = "LustSickPuppy",
            desc = "Artist from Somewhere",
            area = "Somewhere",
            beginArea = "Somewhere Else",
            active = "Yes (2023 - Present)",
            tags = "digital hardcore, hardcore, drum and bass"
        )
    }
}