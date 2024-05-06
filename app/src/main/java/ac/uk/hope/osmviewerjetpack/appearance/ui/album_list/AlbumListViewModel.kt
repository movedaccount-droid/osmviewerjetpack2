package ac.uk.hope.osmviewerjetpack.appearance.ui.album_list

import ac.uk.hope.osmviewerjetpack.repository.MusicBrainzRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository
): ViewModel() {

}