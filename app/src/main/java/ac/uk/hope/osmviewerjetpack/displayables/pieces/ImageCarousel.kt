package ac.uk.hope.osmviewerjetpack.displayables.pieces

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.request.ImageRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: List<Uri>,
    description: String,
    modifier: Modifier = Modifier,
    offset: Dp = 0.dp,
    imageWidth: Dp = 120.dp
) {
    val pagerState = rememberPagerState(
        pageCount = { images.size },
    )
    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fixed(imageWidth),
        beyondBoundsPageCount = 1,
        contentPadding = PaddingValues(start = offset),
        flingBehavior = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(3)
        ),
        modifier = modifier
    ) { page ->
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(images[page])
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp)),
            onError = { err -> err.result.throwable.message?.let { it1 -> Log.d(TAG, it1) } }
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