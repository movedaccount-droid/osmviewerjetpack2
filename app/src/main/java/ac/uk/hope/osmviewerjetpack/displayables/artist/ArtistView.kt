package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ArtistView(
    mbid: String
) {

    val viewModel: ArtistViewViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.getArtist(mbid)
    }

    Column {
        viewModel.artist.value?.also { artist ->
            Text(text = artist.name)
        } ?: run {
            // TODO: loading icon in center of screen
            Text(text = "nothing...")
        }
    }

}