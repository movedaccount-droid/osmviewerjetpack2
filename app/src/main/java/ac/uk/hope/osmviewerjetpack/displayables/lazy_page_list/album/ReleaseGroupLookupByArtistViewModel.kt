package ac.uk.hope.osmviewerjetpack.displayables.lazy_page_list.album

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.ReleaseGroupLookupByArtistPagingSourceFactory
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

@HiltViewModel(assistedFactory =
    ReleaseGroupLookupByArtistViewModel.ReleaseGroupLookupByArtistViewModelFactory::class)
class ReleaseGroupLookupByArtistViewModel
@AssistedInject constructor(
    @Assisted private val mbid: String,
    private val fanartTvRepository: FanartTvRepository,
    private val pagingSourceFactory: ReleaseGroupLookupByArtistPagingSourceFactory
): ViewModel() {

    val flow = Pager(
        PagingConfig(pageSize = 15)
    ) {
        pagingSourceFactory.create(mbid)
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
        fanartTvRepository.getAlbumImages(mbid).map { it.thumbnail }
    }

    @AssistedFactory
    interface ReleaseGroupLookupByArtistViewModelFactory {
        fun create(mbid: String): ReleaseGroupLookupByArtistViewModel
    }
}