package ac.uk.hope.osmviewerjetpack.displayables.search

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 15

@HiltViewModel
class ArtistSearchViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): ViewModel() {

    val artists = mutableStateListOf<Artist>()
    val images = mutableStateMapOf<String, Uri?>()
    val loading = mutableStateOf(false)
    val endOfResults = mutableStateOf(false)

    private var query = ""
    private var page = 0
    private var firstVisibleItem = 0
    private var lastVisibleItem = 0
    private var imageLoadingChainActive = false

    // update the first visible item, used to prioritize image loading
    fun onFirstVisibleItemChanged(firstItem: Int) {
        firstVisibleItem = firstItem
        startImageLoadingChain()
    }

    // update the last visible item, see above
    fun onLastVisibleLoadedItemChanged(lastLoadedItem: Int) {
        // note that the incoming value is not the last index! it is the last of what is on screen
        // i.e. if there are 30 items but only 9 can be seen, this will still always be between 0-9
        lastVisibleItem = firstVisibleItem + lastLoadedItem
        startImageLoadingChain()
    }

    fun sendQuery(newQuery: String) {
        query = newQuery
        getNextPage()
    }

    // get next page of musicbrainz results
    fun getNextPage() {
        Log.d(TAG, "next page called")
        if (!loading.value && !endOfResults.value) {
            viewModelScope.launch {
                val dataResult = musicBrainzRepository.searchArtistsName(
                    query = query,
                    limit = PAGE_SIZE,
                    offset = PAGE_SIZE * page
                ).collect {
                    artists.addAll(it)
                    endOfResults.value = it.size < PAGE_SIZE
                    loading.value = false
                    startImageLoadingChain()
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
    private fun startImageLoadingChain() {
        if (!imageLoadingChainActive && !artists.isEmpty()) {
            imageLoadingChainActive = true
            viewModelScope.launch {
                loadNeededArtistImage()
            }
        }
    }

    // retrieve local/network image for single artist
    private fun makeArtistImageRequest(mbid: String) {
        viewModelScope.launch {
            fanartTvRepository.getArtistImages(mbid)
                .map { it.thumbnail }
                .distinctUntilChanged()
                .collect {
                    images[mbid] = it
                    loadNeededArtistImage()
                }
        }
    }

    // only load images that are currently on screen.
    private fun loadNeededArtistImage() {
        val neededArtistImage = artists.subList(firstVisibleItem, lastVisibleItem).find {
            images[it.mbid] == null
        }
        if (neededArtistImage != null) {
            makeArtistImageRequest(neededArtistImage.mbid)
        } else {
            imageLoadingChainActive = false
        }
    }
}