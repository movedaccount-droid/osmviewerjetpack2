package ac.uk.hope.osmviewerjetpack.displayables.list

import ac.uk.hope.osmviewerjetpack.displayables.pieces.Center
import ac.uk.hope.osmviewerjetpack.displayables.pieces.ListItem
import ac.uk.hope.osmviewerjetpack.displayables.pieces.ListItemInfo
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun ItemList(
    items: Flow<List<ListItemInfo>>,
    getItemIcon: (id: String) -> Flow<Uri>,
    onItemSelected: (id: String) -> Unit = {},
    header: (@Composable ColumnScope.() -> Unit)? = null
) {

    // this sucks but i want to reuse the viewmodel and this flow isn't
    // compatible with the LazyPageList flow. ugghh
    val itemsState = rememberSaveable {
        mutableStateOf<List<ListItemInfo>?>(null)
    }
    LaunchedEffect(Unit) {
        items.collect {
            itemsState.value = it
        }
    }

    // check on-screen for image prioritization
    val getVisibleIds = { startIndex: Int, endIndex: Int ->
        if (
            itemsState.value != null
            && startIndex in 0..endIndex
            && endIndex in itemsState.value!!.indices
        ) {
            itemsState.value!!.subList(startIndex, endIndex).map { it.id }
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
        itemsState.value?.let {
            items(
                count = it.size,
                key = { index -> it[index].id }
            ) { index ->
                val result = it[index]
                ListItem(
                    image = viewModel.images[result.id],
                    headline = result.name,
                    subhead = result.desc,
                    onClick = { onItemSelected(result.id) }
                )
            }
        } ?: run {
            item {
                Center {
                    CircularProgressIndicator()
                }
            }
        }
    }
}