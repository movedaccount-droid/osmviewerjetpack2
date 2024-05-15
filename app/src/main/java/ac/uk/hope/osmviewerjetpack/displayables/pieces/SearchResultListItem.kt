package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun SearchResultListItem(
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
                    image = image
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
private fun PreviewSearchResultListItem() {
    OSMViewerJetpackTheme {
        SearchResultListItem(
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
private fun PreviewSearchResultListItemNoDesc() {
    OSMViewerJetpackTheme {
        SearchResultListItem(
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