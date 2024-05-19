package ac.uk.hope.osmviewerjetpack.displayables.list

import android.net.Uri
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.math.max

@HiltViewModel(assistedFactory = ItemListViewModel.ItemListViewModelFactory::class)
class ItemListViewModel
@AssistedInject constructor(
    @Assisted private val listOffset: Int,
    @Assisted private val getVisibleIds: (firstIndex: Int, lastIndex: Int) -> List<String>,
    @Assisted private val getItemIcon: (id: String) -> Flow<Uri>
): ViewModel() {

    val listState = LazyListState()
    private var visibleIds = listOf<String>()

    val images = mutableStateMapOf<String, Uri?>()
    private var imageLoadingChainActive = false

    // watch list for scrolling
    init {
        viewModelScope.launch {
            snapshotFlow { listState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { updateVisibleIds() }
        }

        viewModelScope.launch {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastIndex }
                .distinctUntilChanged()
                .collect { updateVisibleIds() }
        }
    }

    // we want to prioritize retrieving images that are currently on-screen from the network.
    // we start a chain, load an image, await the response and then load another.
    // there are probably better ways to do this that would allow loading all cached images
    // immediately. but i can't think of them without this getting Very Complicated.
    fun updateVisibleIds() {
        val visibleItemCount = listState.layoutInfo.visibleItemsInfo.lastIndex
        val firstIndex = max(listState.firstVisibleItemIndex - listOffset, 0)
        val lastIndex = firstIndex + visibleItemCount
        // edge case values for empty lists
        if (visibleItemCount != -1 && firstIndex <= lastIndex) {
            visibleIds = getVisibleIds(firstIndex, lastIndex)
            startIconLoadingChain()
        }
    }

    private fun startIconLoadingChain() {
        if (!imageLoadingChainActive) {
            imageLoadingChainActive = true
            viewModelScope.launch {
                loadNeededImage()
            }
        }
    }

    // retrieve local/network image for single artist
    private fun makeIconRequest(id: String) {
        viewModelScope.launch {
            getItemIcon(id)
                .distinctUntilChanged()
                .collect {
                    images[id] = it
                    loadNeededImage()
                }
        }
    }

    // only load images that are currently on screen.
    private fun loadNeededImage() {
        val neededId = visibleIds.find { images[it] == null }
        if (neededId != null) {
            makeIconRequest(neededId)
        } else {
            imageLoadingChainActive = false
        }
    }

    @AssistedFactory
    interface ItemListViewModelFactory {
        fun create(
            listOffset: Int,
            getVisibleIds: (firstIndex: Int, lastIndex: Int) -> List<String>,
            getItemIcon: (id: String) -> Flow<Uri>
        ): ItemListViewModel
    }
}