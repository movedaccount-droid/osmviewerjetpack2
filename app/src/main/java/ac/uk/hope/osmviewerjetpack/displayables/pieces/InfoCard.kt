package ac.uk.hope.osmviewerjetpack.displayables.pieces

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
fun InfoCard(
    icon: Uri?,
    name: String,
    desc: String,
    table: Map<String, String>,
) {
    // TODO: lazy scrollable row of image cards
    // TODO: move table to expandable card
    // TODO: optional button for subscription
    // WE ARE HERE CREATEING THE RELEASE LOOKUIP USING THIS NEWLY SE
    // PARATED CARD. AND THEN FOCUSIN  OG N THAT BUTTON AND SUBSCRIPTIONS
    // AND THEN REGULARLY LERAING CACHES
    // AND THEN TESTS AFTER THAT PROBABLY
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
            table.map {
                InfoLine(it.key, it.value)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewInfoCard() {
    OSMViewerJetpackTheme {
        InfoCard(
            icon = Uri.parse("https://thefader-res.cloudinary.com/private_images/w_760,c_limit,f_auto,q_auto:best/IMG_0301_brrdcn/song-you-need-lustsickpuppy-reclaims-her-iconography.jpg"),
            name = "LustSickPuppy",
            desc = "Artist from Somewhere",
            table = mapOf(
                "Area" to "Somewhere",
                "Began" to "Somewhere Else",
                "Active" to "Yes (2023 - Present)",
                "Tagged" to "digital hardcore, hardcore, drum and bass"
            )
        )
    }
}