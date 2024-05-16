package ac.uk.hope.osmviewerjetpack.displayables.search

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach

// generic search results component.
// retrieves paginated SearchResults from a SearchViewSearcher, which can be extended
// to provide any api request. after retrieval, images are loaded one-by-one,
// prioritized by what is on-screen through the same object. items tapped return their id through
// a callback.

@Composable
fun Search(
    resultFlow: Flow<PagingData<SearchResult>>,
    getItemIcon: (id: String) -> Flow<Uri>,
    onItemSelected: (id: String) -> Unit = {}
) {

    val viewModel =
        hiltViewModel<SearchViewModel, SearchViewModel.SearchViewModelFactory> { factory ->
        factory.create(getItemIcon)
    }
    val lazyPagingItems = resultFlow.collectAsLazyPagingItems()

    // check what is on-screen for image prioritization
    // this is almost business logic, but we need to collectAsLazyPagingItems in a composable,
    // so we can't put it in the viewmodel. blugh
    fun recalculateVisibleIds() {
        // since we're using a lazylist, items are unloaded as we scroll forward.
        // .lastIndex refers to loaded items, not items in the list!
        val firstVisibleId = viewModel.listState.firstVisibleItemIndex
        val lastVisibleId = firstVisibleId + viewModel.listState.layoutInfo.visibleItemsInfo.lastIndex
        if (lastVisibleId >= 0) {
            viewModel.updateVisibleIds(
                lazyPagingItems.itemSnapshotList.subList(
                    firstVisibleId,
                    lastVisibleId
                ).map { it!!.id } // our root list isn't nullable. when will this ever be null??
            )
        }
    }

    LaunchedEffect(viewModel.listState) {
        snapshotFlow { viewModel.listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { recalculateVisibleIds() }
    }

    LaunchedEffect(viewModel.listState) {
        snapshotFlow { viewModel.listState.layoutInfo.visibleItemsInfo.lastIndex }
            .distinctUntilChanged()
            .onEach { recalculateVisibleIds() }
    }

    // render list
    LazyColumn(
        state = viewModel.listState
    ) {
        items(count = lazyPagingItems.itemCount) { index ->
            val result = lazyPagingItems[index]!!
            SearchResultListItem(
                image = viewModel.images[result.id],
                headline = result.name,
                subhead = result.desc,
                onClick = { onItemSelected(result.id) }
            )
        }
        if (lazyPagingItems.loadState.append.endOfPaginationReached) {
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
        }
        if (lazyPagingItems.loadState.append == LoadState.Loading) {
            item {
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(16.dp)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        } else {
            recalculateVisibleIds()
        }
    }
}