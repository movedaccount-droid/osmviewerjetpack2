package ac.uk.hope.osmviewerjetpack.displayables.album_list

import ac.uk.hope.osmviewerjetpack.R
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
import android.widget.ImageView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ArtistList() {
    val viewModel: ArtistListViewModel = viewModel()
    Log.d("HI", "view model is $viewModel")
    Text(
        "testing"
    )
    LazyColumn {
        itemsIndexed(viewModel.artists) { index, artist ->
            viewModel.onChangeArtistListScrollPosition(index)
            Text(text = artist.name)
        }
    }
    viewModel.images?.let {
        Text(
            it.banner.toString()
        )
        Log.d(TAG, "loading ${it.banner.first()}")
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(it.banner.first())
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "some",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth(),
            onError = { err -> err.result.throwable.message?.let { it1 -> Log.d(TAG, it1) } }
        )
    }
}