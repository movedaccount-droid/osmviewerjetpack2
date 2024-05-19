package ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list

import ac.uk.hope.osmviewerjetpack.displayables.list.ItemListViewModel
import ac.uk.hope.osmviewerjetpack.displayables.pieces.ListItem
import ac.uk.hope.osmviewerjetpack.displayables.pieces.ListItemInfo
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlin.math.min

// generic search results component.
// retrieves paginated SearchResults from a SearchViewSearcher, which can be extended
// to provide any api request. after retrieval, images are loaded one-by-one,
// prioritized by what is on-screen through the same object. items tapped return their id through
// a callback.

@Composable
fun LazyPageList(
    pageFlow: Flow<PagingData<ListItemInfo>>,
    getItemIcon: (id: String) -> Flow<Uri>,
    onItemSelected: (id: String) -> Unit = {},
    defaultIcon: ImageVector = Icons.Default.Face,
    header: (@Composable ColumnScope.() -> Unit)? = null
) {

    // check what is on-screen for image prioritization
    val lazyPagingItems = pageFlow.collectAsLazyPagingItems()
    val getVisibleIds = { startIndex: Int, endIndex: Int ->
        val count = lazyPagingItems.itemCount
        if (count > 0) {
            (startIndex..min(endIndex, count - 1)).map {
                lazyPagingItems.peek(it)!!.id
            }
        } else {
            listOf()
        }
    }

    val viewModel =
        hiltViewModel<ItemListViewModel, ItemListViewModel.ItemListViewModelFactory> { factory ->
        factory.create(
            listOffset = if (header == null) 0 else 1,
            getVisibleIds = getVisibleIds,
            getItemIcon = getItemIcon
        )
    }

    // render list
    LazyColumn(
        state = viewModel.listState
    ) {
        header?.let {
            item {
                Column {
                    it(this)
                }
            }
        }
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id }
        ) { index ->
            val result = lazyPagingItems[index]!!
            ListItem(
                image = viewModel.images[result.id],
                headline = result.name,
                subhead = result.desc,
                defaultIcon = defaultIcon,
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
        if (lazyPagingItems.loadState.append == LoadState.Loading
            || lazyPagingItems.loadState.refresh == LoadState.Loading) {
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
        }
    }
}