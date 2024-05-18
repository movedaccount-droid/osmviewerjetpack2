package ac.uk.hope.osmviewerjetpack.displayables.release

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.AlbumImages
import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Release
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReleaseViewViewModel.ReleaseViewViewModelFactory::class)
class ReleaseViewViewModel
@AssistedInject constructor(
    @Assisted private val releaseGroupMbid: String,
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository,
): ViewModel() {

    private val _release = mutableStateOf<Pair<ReleaseGroup, Release>?>(null)
    val release: State<Pair<ReleaseGroup, Release>?> = _release

    private val _releaseImages = mutableStateOf<AlbumImages?>(null)
    val releaseImages: State<AlbumImages?> = _releaseImages

    init {
        viewModelScope.launch {
            musicBrainzRepository.getReleaseWithReleaseGroup(releaseGroupMbid).collect {
                _release.value = it
            }
        }
        viewModelScope.launch {
            fanartTvRepository.getAlbumImages(releaseGroupMbid).collect {
                _releaseImages.value = it
            }
        }
    }

    @AssistedFactory
    interface ReleaseViewViewModelFactory {
        fun create(releaseGroupMbid: String): ReleaseViewViewModel
    }
}