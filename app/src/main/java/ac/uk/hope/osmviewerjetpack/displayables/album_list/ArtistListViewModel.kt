package ac.uk.hope.osmviewerjetpack.displayables.album_list

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.repository.fanarttv.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.repository.musicbrainz.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

const val PAGE_SIZE = 30

@HiltViewModel
class ArtistListViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): ViewModel() {

    private val _artists = mutableStateListOf<Artist>()
    private val _images = mutableStateMapOf<String, Uri?>()

    val artists
        get() = _artists

    val images
        get() = _images

    val page = mutableStateOf(0)
    var scrollPosition = 0
    private var musicBrainzLoading = false
    private var fanartTvLoading = false

    fun onChangeArtistListScrollPosition(position: Int) {
        scrollPosition = position
        if (scrollPosition + 1 >= page.value * PAGE_SIZE && !musicBrainzLoading) {
            Log.d(TAG, "scrolled far enough")
            runSearch()
        }
    }

    // make request, push result and start next request
    private fun makeArtistImageRequest(mbid: String) {
        viewModelScope.launch {
            try {
                val imageResult = fanartTvRepository.getArtistImages(mbid)
                _images[mbid] = imageResult.thumbnail?.first()
                    ?: imageResult.background?.first()
                    ?: imageResult.hdLogo?.first()
                    ?: imageResult.logo?.first()
                    ?: imageResult.banner?.first()
            } catch (e: Throwable) {
                // TODO: replace with a sane, local default
                _images[mbid] = Uri.parse(
                    Uri.decode(
                        "https://www.svgrepo.com/show/401366/cross-mark-button.svg"
                    )
                )
            }

        }.invokeOnCompletion { getNextArtistImage() }
    }

    // start the next request prioritizing what is already on screen
    private fun getNextArtistImage() {
        if (!fanartTvLoading) {
            val priorityOrder = artists.subList(scrollPosition, artists.size)
            priorityOrder.addAll(artists.subList(0, scrollPosition))
            priorityOrder.find {
                images[it.id] == null
            }?.let {
                makeArtistImageRequest(it.id)
            }
        }
    }

    private fun runSearch() {
        viewModelScope.launch {
            val dataResult = musicBrainzRepository.searchArtistsName(
                query = "red",
                limit = PAGE_SIZE,
                offset = PAGE_SIZE * page.value
            )
            _artists.addAll(dataResult)
            musicBrainzLoading = false
            getNextArtistImage()
        }
        musicBrainzLoading = true
        page.value += 1
    }

    init {
        runSearch()
    }
}