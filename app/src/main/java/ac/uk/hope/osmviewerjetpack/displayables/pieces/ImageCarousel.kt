package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: List<Uri>,
    description: String,
    modifier: Modifier = Modifier,
    offset: Dp = 8.dp,
    imageWidth: Dp = 100.dp
) {
    val pagerState = rememberPagerState(
        pageCount = { images.size },
    )
    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(imageWidth + offset),
        beyondBoundsPageCount = 1,
        contentPadding = PaddingValues(start = offset),
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(3)
        ),
        modifier = modifier
    ) { page ->
        ThrobberImage(
            image = images[page],
            description = description,
            modifier = Modifier
                .padding(end = offset)
                .fillMaxSize(),
            defaultIcon = Icons.Default.Clear
        )
    }
}

@Preview
@Composable
private fun PreviewImageCarousel() {
    val uri = Uri.parse(
        Uri.decode(
            "https://www.cameraegg.org/wp-content/uploads/2016/01/Nikon-D500-Sample-Images-2.jpg"
        )
    )
    OSMViewerJetpackTheme {
        ImageCarousel(
            images = listOf(uri, uri, uri, uri, uri),
            description = "some",
        )
    }
}