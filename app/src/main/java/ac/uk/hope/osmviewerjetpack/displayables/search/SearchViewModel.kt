package ac.uk.hope.osmviewerjetpack.displayables.search

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzArtistSearchPagingSource
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzArtistSearchPagingSourceFactory
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val pagingSourceFactory: MusicBrainzArtistSearchPagingSourceFactory
): ViewModel() {

    private lateinit var searcher: SearchViewSearcher

    val flow = Pager(
        PagingConfig(pageSize = 15)
    ) {
        pagingSourceFactory.create("radiohead")
    }.flow.cachedIn(viewModelScope).map { pagingData ->
        pagingData.map {
            SearchResult(
                id = it.mbid,
                name = it.name,
                desc = it.shortDesc
            )
        }
    }

    val results = mutableStateListOf<SearchResult>()
    val images = mutableStateMapOf<String, Uri?>()
    val loading = mutableStateOf(false)
    val endOfResults = mutableStateOf(false)

    private var page = 0
    private var firstVisibleItem = 0
    private var lastVisibleItem = 0
    private var imageLoadingChainActive = false

    // provide searcher. i think this architecture is generally gross... there's probably a better
    // way to genericize viewmodels than this
    fun applySearcher(newSearcher: SearchViewSearcher) {
        searcher = newSearcher
    }

    // update the first visible item, used to prioritize image loading
    fun onFirstVisibleItemChanged(firstItem: Int) {
        firstVisibleItem = firstItem
        startIconLoadingChain()
    }

    // update the last visible item, see above
    fun onLastVisibleLoadedItemChanged(lastLoadedItem: Int) {
        // note that the incoming value is not the last index! it is the last of what is on screen
        // i.e. if there are 30 items but only 9 can be seen, this will still always be between 0-9
        lastVisibleItem = firstVisibleItem + lastLoadedItem
        startIconLoadingChain()
    }

    // get next page of results from searcher
    fun getNextPage() {
        if (!loading.value && !endOfResults.value) {
            viewModelScope.launch {
                searcher.getPage(page).collect {
                    results.addAll(it)
                    endOfResults.value = it.size < searcher.pageSize
                    loading.value = false
                    startIconLoadingChain()
                }
            }
            loading.value = true
            page += 1
        }
    }

    // we want to prioritize retrieving images that are currently on-screen from the network.
    // we start a chain, load an image, await the response and then load another.
    // there are probably better ways to do this that would allow loading all cached images
    // immediately. but i can't think of them without this getting Very Complicated.
    private fun startIconLoadingChain() {
        if (!imageLoadingChainActive && !results.isEmpty()) {
            imageLoadingChainActive = true
            viewModelScope.launch {
                loadNeededImage()
            }
        }
    }

    // retrieve local/network image for single artist
    private fun makeIconRequest(id: String) {
        viewModelScope.launch {
            searcher.getIcon(id)
                .distinctUntilChanged()
                .collect {
                    images[id] = it
                    loadNeededImage()
                }
        }
    }

    // only load images that are currently on screen.
    private fun loadNeededImage() {
        val neededArtistImage = results.subList(firstVisibleItem, lastVisibleItem).find {
            images[it.id] == null
        }
        if (neededArtistImage != null) {
            makeIconRequest(neededArtistImage.id)
        } else {
            imageLoadingChainActive = false
        }
    }
}