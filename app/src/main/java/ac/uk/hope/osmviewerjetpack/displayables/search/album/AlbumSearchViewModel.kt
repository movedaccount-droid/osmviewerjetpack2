package ac.uk.hope.osmviewerjetpack.displayables.search.album

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzArtistSearchPagingSourceFactory
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.displayables.search.SearchResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AlbumSearchViewModel
@Inject constructor(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository,
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