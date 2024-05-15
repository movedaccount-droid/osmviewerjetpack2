package ac.uk.hope.osmviewerjetpack.displayables.search.artist

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistSearchViewModel
@Inject constructor(
    musicBrainzRepository: MusicBrainzRepository,
    fanartTvRepository: FanartTvRepository
): ViewModel() {
    private val searcher = ArtistSearchViewSearcher(
        musicBrainzRepository,
        fanartTvRepository
    )

    fun getSearcher(query: String): ArtistSearchViewSearcher {
        searcher.query = query
        return searcher
    }
}