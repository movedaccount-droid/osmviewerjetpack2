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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun IconImage(
    image: Uri?,
    description: String,
    modifier: Modifier = Modifier,
    defaultIcon: ImageVector = Icons.Default.Face,
) {
    ThrobberImage(
        image = image,
        description = description,
        modifier = modifier
            .size(72.dp, 72.dp),
        defaultIcon = defaultIcon
    )
}