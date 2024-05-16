package ac.uk.hope.osmviewerjetpack.displayables.search

import ac.uk.hope.osmviewerjetpack.ui.theme.OSMViewerJetpackTheme
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach

// generic search results component.
// retrieves paginated SearchResults from a SearchViewSearcher, which can be extended
// to provide any api request. after retrieval, images are loaded one-by-one,
// prioritized by what is on-screen through the same object. items tapped return their id through
// a callback.

@Composable
fun Search(
    searcher: SearchViewSearcher,
    resultFlow: Flow<PagingData<SearchResult>>,
    onItemSelected: (String) -> Unit = {}
) {

    val viewModel =
        hiltViewModel<SearchViewModel, SearchViewModel.SearchViewModelFactory> { factory ->
        factory.create(searcher, resultFlow)
    }
    val lazyPagingItems = resultFlow.collectAsLazyPagingItems()

    // this sucks. we can only index the paged items from our lazypageditems,
    // which we can only instantiate in the composable.
    // this isn't technically business logic but i'm still not happy
    fun recalculateVisibleIds() {
        // since we're using a lazylist, items are unloaded as we scroll forward.
        // lastindex refers to loaded items, not items in the list!
        val firstVisibleId = viewModel.listState.firstVisibleItemIndex
        val lastVisibleId =
            firstVisibleId + viewModel.listState.layoutInfo.visibleItemsInfo.lastIndex
        if (lastVisibleId < 0) return
        viewModel.updateVisibleIds(
            lazyPagingItems.itemSnapshotList.subList(
                firstVisibleId,
                lastVisibleId
            ).map { it!!.id } // our root list isn't nullable. when will this ever be null??
        )
    }

    // TODO: we can surely use a lesser heuristic to identify when to recheck what is visible
    // on-screen...
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