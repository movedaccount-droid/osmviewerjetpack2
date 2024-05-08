package ac.uk.hope.osmviewerjetpack.displayables.search

import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.repository.fanarttv.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.repository.musicbrainz.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 30
// TODO: replace with a sane, local default
val BACKUP_IMAGE_URI = Uri.parse(
    Uri.decode(
        "https://www.svgrepo.com/show/401366/cross-mark-button.svg"
    )
)

@HiltViewModel
class ArtistListViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): ViewModel() {

    val artists = mutableStateListOf<Artist>()
    val images = mutableStateMapOf<String, Uri?>()
    val loading = mutableStateOf(false)
    val endOfResults = mutableStateOf(false)

    private var page = 0
    private var firstVisibleItem = 0
    private var lastVisibleItem = 0
    private var fanartTvLoading = false

    // update the first visible item, used to prioritize image loading
    fun onFirstVisibleItemChanged(firstItem: Int) {
        firstVisibleItem = firstItem
        loadNeededArtistImage()
    }

    // update the last visible item, see above
    fun onLastVisibleLoadedItemChanged(lastLoadedItem: Int) {
        // note that the incoming value is not the last index! it is the last of what is on screen
        // i.e. if there are 30 items but only 9 can be seen, this will still always be between 0-9
        lastVisibleItem = firstVisibleItem + lastLoadedItem
        loadNeededArtistImage()
    }

    // get next page of musicbrainz results
    fun getNextPage() {
        Log.d(TAG, "next page called")
        if (!loading.value && !endOfResults.value) {
            viewModelScope.launch {
                val dataResult = musicBrainzRepository.searchArtistsName(
                    query = "red",
                    limit = PAGE_SIZE,
                    offset = PAGE_SIZE * page
                )
                artists.addAll(dataResult)
                endOfResults.value = dataResult.size < PAGE_SIZE
                loading.value = false
                loadNeededArtistImage()
            }
            loading.value = true
            page += 1
        }
    }


    // make request, push result and start next request
    private fun makeArtistImageRequest(mbid: String) {
        fanartTvLoading = true
        viewModelScope.launch {
            try {
                val imageResult = fanartTvRepository.getArtistImages(mbid)
                images[mbid] = imageResult.thumbnail?.first()
                    ?: imageResult.background?.first()
                    ?: imageResult.hdLogo?.first()
                    ?: imageResult.logo?.first()
                    ?: imageResult.banner?.first()
                    ?: BACKUP_IMAGE_URI
            } catch (e: Throwable) {
                images[mbid] = BACKUP_IMAGE_URI
            }
        }.invokeOnCompletion {
            fanartTvLoading = false
            loadNeededArtistImage()
        }
    }

    // start the next image request if any artists on-screen have not yet been loaded
    // this helps avoid mashing the api with requests the user won't look at (i.e. changes tabs)
    private fun loadNeededArtistImage() {
        if (!fanartTvLoading) {
            artists.subList(firstVisibleItem, lastVisibleItem).find {
                images[it.id] == null
            }?.let {
                makeArtistImageRequest(it.id)
            }
        }
    }

    init {
        getNextPage()
    }
}