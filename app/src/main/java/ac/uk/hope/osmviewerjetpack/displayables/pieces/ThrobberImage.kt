package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun ThrobberImage(
    image: Uri?,
    description: String,
    modifier: Modifier = Modifier,
    defaultIcon: ImageVector = Icons.Default.Face
) {

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .build(),
        loading = {
            Center {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(8.dp)
                )
            }
        },
        // only show default icon if a uri was supplied but failed
        error = {
            if (image == null) {
                Center {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(8.dp)
                    )
                }
            } else {
                Center {
                    Icon(
                        imageVector = defaultIcon,
                        contentDescription = description,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        contentDescription = description,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        onError = { err -> err.result.throwable.message?.let { it1 -> Log.d(TAG, it1) } }
    )

}