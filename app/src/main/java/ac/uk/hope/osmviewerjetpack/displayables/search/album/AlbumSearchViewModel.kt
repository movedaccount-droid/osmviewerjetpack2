package ac.uk.hope.osmviewerjetpack.displayables.search.album

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumSearchViewModel
@Inject constructor(
    musicBrainzRepository: MusicBrainzRepository,
    fanartTvRepository: FanartTvRepository
): ViewModel() {
    private val searcher = AlbumSearchViewSearcher(
        musicBrainzRepository,
        fanartTvRepository
    )

    fun getSearcher(artistMbid: String): AlbumSearchViewSearcher {
        searcher.artistMbid = artistMbid
        return searcher
    }
}