package ac.uk.hope.osmviewerjetpack.displayables.album_list

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.repository.fanarttv.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.repository.musicbrainz.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.util.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PAGE_SIZE = 30

@HiltViewModel
class ArtistListViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): ViewModel() {

    private val _artists = mutableStateListOf<Artist>()
    private val _images = mutableStateOf<ArtistImages?>(null)

    val page = mutableStateOf(0)
    private var loading = false

    val artists
        get() = _artists

    val images
        get() = _images.value

    fun onChangeArtistListScrollPosition(position: Int) {
        Log.d(TAG, position.toString())
        if (position + 1 >= page.value * PAGE_SIZE) {
            runSearch()
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
            loading = false // data and image loading can overlap, no problem
            val imageResult = fanartTvRepository.getArtistImages("f4a31f0a-51dd-4fa7-986d-3095c40c5ed9")
            _images.value = imageResult
        }
        loading = true
        page.value += 1
    }

    init {
        runSearch()
    }
}