package ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list.artist

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.ArtistSearchPagingSourceFactory
import ac.uk.hope.osmviewerjetpack.displayables.pieces.ListItemInfo
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

@HiltViewModel(assistedFactory = ArtistSearchViewModel.ArtistSearchViewModelFactory::class)
class ArtistSearchViewModel
@AssistedInject constructor(
    @Assisted private val query: String,
    private val fanartTvRepository: FanartTvRepository,
    private val pagingSourceFactory: ArtistSearchPagingSourceFactory
): ViewModel() {

    val flow = Pager(
        PagingConfig(pageSize = 15)
    ) {
        pagingSourceFactory.create(query)
    }.flow.cachedIn(viewModelScope).map { pagingData ->
        pagingData.map {
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

    @AssistedFactory
    interface ArtistSearchViewModelFactory {
        fun create(query: String): ArtistSearchViewModel
    }
}