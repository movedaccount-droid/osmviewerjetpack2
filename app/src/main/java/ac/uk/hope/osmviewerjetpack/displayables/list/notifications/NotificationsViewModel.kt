package ac.uk.hope.osmviewerjetpack.displayables.list.notifications

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.displayables.pieces.ListItemInfo
import ac.uk.hope.osmviewerjetpack.util.toDateString
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository,
): ViewModel() {

    private val _listCount = mutableStateOf<Int?>(null)
    val listCount: Int?
        get() = _listCount.value

    val flow = musicBrainzRepository.getDetailedNotifications()
        .map { list ->
            list.map { notification ->
                ListItemInfo(
                    id = notification.releaseGroup.mbid,
                    name = notification.releaseGroup.name,
                    desc = "Added ${notification.sent.toDateString()}"
                )
            }
        }

    // so that we collect list sizes even when the list itself is not rendered
    init {
        viewModelScope.launch {
            flow.collect {
                _listCount.value = it.size
            }
        }
    }

    val getItemIcon = { mbid: String ->
        fanartTvRepository.getAlbumImages(mbid).map { it.thumbnail }
    }

    fun removeNotification(mbid: String) {
        viewModelScope.launch {
            musicBrainzRepository.removeNotification(mbid)
        }
    }

}