package ac.uk.hope.osmviewerjetpack.displayables.album_list

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.repository.fanarttv.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.repository.musicbrainz.MusicBrainzRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): ViewModel() {

    private val _artists = mutableStateOf(listOf<Artist>())
    private val _images = mutableStateOf<ArtistImages?>(null)

    val artists
        get() = _artists.value

    val images
        get() = _images.value

    init {
        viewModelScope.launch {
            val dataResult = musicBrainzRepository.searchArtistsName("red")
            _artists.value = dataResult
            val imageResult = fanartTvRepository.getArtistImages("f4a31f0a-51dd-4fa7-986d-3095c40c5ed9")
            _images.value = imageResult
        }
    }
}