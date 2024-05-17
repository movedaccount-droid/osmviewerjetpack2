package ac.uk.hope.osmviewerjetpack.displayables.list.followedArtist

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.displayables.pieces.ListItemInfo
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FollowedArtistBrowseViewModel
@Inject constructor(
    musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository,
): ViewModel() {

    val flow = musicBrainzRepository.getFollowedArtists().map {
        list -> list.map {
            ListItemInfo(
                id = it.mbid,
                name = it.name,
                desc = it.shortDesc
            )
        }
    }

    val getItemIcon = { mbid: String ->
        fanartTvRepository.getArtistImages(mbid).map { it.thumbnail }
    }

}