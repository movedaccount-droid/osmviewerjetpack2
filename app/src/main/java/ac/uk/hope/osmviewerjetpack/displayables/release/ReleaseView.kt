package ac.uk.hope.osmviewerjetpack.displayables.release

import ac.uk.hope.osmviewerjetpack.displayables.pieces.InfoCard
import ac.uk.hope.osmviewerjetpack.displayables.pieces.InfoLine
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReleaseView(
    releaseGroupMbid: String
) {

    val viewModel = hiltViewModel<ReleaseViewViewModel,
            ReleaseViewViewModel.ReleaseViewViewModelFactory> { factory ->
        factory.create(releaseGroupMbid)
    }

    viewModel.release.value?.let {
        val table = mutableMapOf(
            "Format" to it.second.format,
            "Country" to it.second.country,
            "Status" to it.second.status,
        )
        it.second.barcode?.let { barcode ->
            table["Barcode"] = barcode.toString()
        }
        it.second.packaging?.let { packaging ->
            table["Packaging"] = packaging
        }
        it.second.disambiguation?.let { disambiguation ->
            table["Description"] = disambiguation
        }
        if (it.first.types.firstOrNull() != null) {
            table["Type"] = it.first.types.joinToString(", ")
        }
        LazyColumn {
            item {
                InfoCard(
                    icon = viewModel.releaseImages.value?.thumbnail,
                    name = it.first.name,
                    desc = it.first.shortDesc,
                    table = table,
                )
            }
            item {
                Text(
                    text = "Tracks",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(it.second.tracks) { track ->
                InfoLine(
                    title = track.name,
                    text = track.length.inWholeSeconds.seconds.toString()
                )
            }
        }
    } ?: run {
        // TODO: loading
    }
}