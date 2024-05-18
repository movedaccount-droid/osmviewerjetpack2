package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListItem(
    image: Uri?,
    headline: String,
    subhead: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column {
        ListItem(
            headlineContent = { Text(headline) },
            supportingContent = {
                subhead?.let {
                    Text(subhead)
                }
            },
            trailingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "arrow"
                )
            },
            leadingContent = {
                IconImage(
                    image = image,
                    description = "$headline icon"
                )
            },
            modifier = modifier.clickable(
                enabled = true,
                onClickLabel = "Open $headline",
                role = Role.Button,
                onClick),
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun PreviewListItem() {
    OSMViewerJetpackTheme {
        ListItem(
            image = Uri.parse(
                Uri.decode(
                    "https://assets.fanart.tv/fanart/music/a9100753-f539-43cf-bcc9-579566fb512e/artistthumb/simply-red-4fded0c91fe47.jpg"
                )
            ),
            headline = "Radiohead",
            subhead = "pre‐Radiohead group, until 1991",
        )
    }
}

@Preview
@Composable
private fun PreviewListItemNoDesc() {
    OSMViewerJetpackTheme {
        ListItem(
            image = Uri.parse(
                Uri.decode(
                    "https://assets.fanart.tv/fanart/music/a9100753-f539-43cf-bcc9-579566fb512e/artistthumb/simply-red-4fded0c91fe47.jpg"
                )
            ),
            headline = "Radiohead",
            subhead = null,
        )
    }
}