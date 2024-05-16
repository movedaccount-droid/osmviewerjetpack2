package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// TODO: it would be nice if the description texts were all aligned
// when in a collection.
// this requires manual measurements and a custom layout - not trivial.
// https://stackoverflow.com/questions/68143308/how-do-i-create-a-table-in-jetpack-compose

@Composable
fun InfoLine(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface (
        modifier
    ) {
        Column {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .alignByBaseline()
                )
                Text (
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.alignByBaseline()
                )
            }
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun PreviewInfoLine() {
    OSMViewerJetpackTheme {
        InfoLine(title = "Location", text = "Manchester")
    }
}

@Preview
@Composable
private fun PreviewInfoLineCollection() {
    OSMViewerJetpackTheme {
        Column {
            InfoLine(title = "Location", text = "Manchester")
            InfoLine(title = "Other", text = "Another Line")
            InfoLine(title = "Something Else", text = "Something Else")
            InfoLine(title = "Another Example", text = "Another Example...")
        }
    }
}