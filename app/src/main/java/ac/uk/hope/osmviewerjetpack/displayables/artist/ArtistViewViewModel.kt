package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = ArtistViewViewModel.ArtistViewViewModelFactory::class)
class ArtistViewViewModel
@AssistedInject constructor(
    @Assisted private val mbid: String,
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): ViewModel() {

    val artist = mutableStateOf<Artist?>(null)
    val artistImages = mutableStateOf<ArtistImages?>(null)
    val artistFollowed = mutableStateOf(false)

    init {
        viewModelScope.launch {
            musicBrainzRepository.getArtist(mbid).collect {
                artist.value = it
            }
        }
        viewModelScope.launch{
            fanartTvRepository.getArtistImages(mbid).collect {
                artistImages.value = it
            }
        }
        viewModelScope.launch{
            musicBrainzRepository.isArtistFollowed(mbid).collect {
                artistFollowed.value = it
            }
        }
    }

    @AssistedFactory
    interface ArtistViewViewModelFactory {
        fun create(mbid: String): ArtistViewViewModel
    }
}