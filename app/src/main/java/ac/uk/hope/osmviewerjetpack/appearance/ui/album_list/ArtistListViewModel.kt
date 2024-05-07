package ac.uk.hope.osmviewerjetpack.appearance.ui.album_list

import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist
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
    private val musicBrainzRepository: MusicBrainzRepository
): ViewModel() {

    private val _artists = mutableStateOf(listOf<Artist>())

    val artists
        get() = _artists.value

    init {
        viewModelScope.launch {
            val result = musicBrainzRepository.searchArtistsName("red")
            _artists.value = result
        }
    }
}