package ac.uk.hope.osmviewerjetpack.appearance.ui.album_list

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AlbumList() {
    val viewModel: AlbumListViewModel = viewModel()
    Log.d("HI", "view model is $viewModel")
    Text(
        "testing"
    )
}