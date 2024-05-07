package ac.uk.hope.osmviewerjetpack.appearance.ui.album_list

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArtistList() {
    val viewModel: ArtistListViewModel = viewModel()
    Log.d("HI", "view model is $viewModel")
    Text(
        "testing"
    )
    LazyColumn {
        items(viewModel.artists) { artist ->
            Text(text = artist.name)
        }
    }
}