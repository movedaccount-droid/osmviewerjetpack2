package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

// TODO: we aren't using this right now. do we need it?
@Composable
fun SearchResultHeaderCard(
    image: Uri?,
    headline: String,
    subhead: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onButtonPress: () -> Unit = {},
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "some",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            onError = { err -> err.result.throwable.message?.let { it1 -> Log.d(TAG, it1) } }
        )
        Text(
            text = headline,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = subhead,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(12.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )
        Row (
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledTonalButton(
                onClick = onButtonPress,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = buttonText,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSearchResultHeaderCard() {
    OSMViewerJetpackTheme {
        SearchResultHeaderCard(
            image = Uri.parse(
                Uri.decode(
                    "https://assets.fanart.tv/fanart/music/a9100753-f539-43cf-bcc9-579566fb512e/artistthumb/simply-red-4fded0c91fe47.jpg"
                )
            ),
            headline = "Radiohead",
            subhead = "pre‐Radiohead group, until 1991",
            buttonText = "View"
        )
    }
}