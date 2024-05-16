package ac.uk.hope.osmviewerjetpack.displayables.search.artist

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
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel(assistedFactory = ArtistSearchViewModel.ArtistSearchViewModelFactory::class)
class ArtistSearchViewModel
@AssistedInject constructor(
    @Assisted private val query: String,
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository,
    private val pagingSourceFactory: MusicBrainzArtistSearchPagingSourceFactory
): ViewModel() {

    val flow = Pager(
        PagingConfig(pageSize = 15)
    ) {
        pagingSourceFactory.create(query)
    }.flow.cachedIn(viewModelScope).map { pagingData ->
        pagingData.map {
            SearchResult(
                id = it.mbid,
                name = it.name,
                desc = it.shortDesc
            )
        }
    }

    val searcher = ArtistSearchViewSearcher(
        query,
        musicBrainzRepository,
        fanartTvRepository
    )

    @AssistedFactory
    interface ArtistSearchViewModelFactory {
        fun create(query: String): ArtistSearchViewModel
    }
}