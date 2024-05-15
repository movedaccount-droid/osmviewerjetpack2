package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun IconImage(
    image: Uri?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .build(),
        placeholder = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "some",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(72.dp, 72.dp)
            .clip(RoundedCornerShape(12.dp)),
        onError = { err -> err.result.throwable.message?.let { it1 -> Log.d(TAG, it1) } }
    )
}