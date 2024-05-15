package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): ViewModel() {

    val artist = mutableStateOf<Artist?>(null)
    val artistImages = mutableStateOf<ArtistImages?>(null)

    fun getArtist(mbid: String) {
        getArtistImages(mbid)
        viewModelScope.launch {
            musicBrainzRepository.getArtist(mbid).collect {
                artist.value = it
            }
        }
    }

    private fun getArtistImages(mbid: String) {
        viewModelScope.launch{
            fanartTvRepository.getArtistImages(mbid).collect {
                artistImages.value = it
            }
        }
    }

}