package ac.uk.hope.osmviewerjetpack.displayables.search

import ac.uk.hope.osmviewerjetpack.displayables.pieces.SearchResultListItem
import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun ArtistSearch(
    query: String,
    onArtistSelected: (String) -> Unit = {}
) {

    val viewModel: ArtistSearchViewModel = hiltViewModel()
    val listState = rememberLazyListState()

    // LaunchedEffect runs when its given dependency changes.
    // therefore Unit forces it to only run once
    LaunchedEffect(Unit) {
        viewModel.sendQuery(query)
    }

    // for some reason combining these makes only the first flow run. weird
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                index -> viewModel.onFirstVisibleItemChanged(index)
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastIndex }
            .distinctUntilChanged()
            .collect {
                index -> viewModel.onLastVisibleLoadedItemChanged(index)
            }
    }
    
    LaunchedEffect(listState) {
        snapshotFlow { listState.canScrollForward }
            .distinctUntilChanged()
            .filter { !it }
            .collect {
                viewModel.getNextPage()
            }
    }

    LazyColumn(
        state = listState
    ) {
        items(viewModel.artists) {artist ->
            SearchResultListItem(
                image = viewModel.images[artist.mbid],
                headline = artist.name,
                subhead = artist.shortDesc,
                onClick = { onArtistSelected(artist.mbid) }
            )
        }
        if (viewModel.endOfResults.value) {
            item {
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "End of Results",
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        } else if (viewModel.loading.value) {
            item {
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(16.dp).padding(16.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewArtistList() {
    OSMViewerJetpackTheme {
        ArtistSearch("red")
    }
}