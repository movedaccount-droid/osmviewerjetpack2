package ac.uk.hope.osmviewerjetpack.displayables.artist

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.mbv.repository.WorkManagerRepository
import ac.uk.hope.osmviewerjetpack.data.external.mbv.repository.WorkManagerRepositoryImpl
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ArtistViewViewModel.ArtistViewViewModelFactory::class)
class ArtistViewViewModel
@AssistedInject constructor(
    @Assisted private val mbid: String,
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository,
    private val workManagerRepository: WorkManagerRepository
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
            musicBrainzRepository.isFollowed(mbid).collect {
                artistFollowed.value = it
            }
        }
    }

    fun toggleFollow() {
        // set artistFollowed immediately for responsiveness
        if (artistFollowed.value) {
            viewModelScope.launch {
                musicBrainzRepository.unfollowArtist(mbid)
            }
        } else {
            viewModelScope.launch {
                musicBrainzRepository.followArtist(mbid)
                workManagerRepository.startSyncWorker()
            }
        }
        artistFollowed.value = !artistFollowed.value
    }

    @AssistedFactory
    interface ArtistViewViewModelFactory {
        fun create(mbid: String): ArtistViewViewModel
    }
}