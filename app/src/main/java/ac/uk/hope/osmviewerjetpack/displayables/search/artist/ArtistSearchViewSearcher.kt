package ac.uk.hope.osmviewerjetpack.displayables.search.artist

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.displayables.search.SearchResult
import ac.uk.hope.osmviewerjetpack.displayables.search.SearchViewSearcher
import android.net.Uri
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArtistSearchViewSearcher(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): SearchViewSearcher {

    override val pageSize = 15
    lateinit var query: String

    override fun getPage(page: Int): Flow<List<SearchResult>> {
        return musicBrainzRepository.searchArtistsName(
            query = query,
            limit = pageSize,
            offset = pageSize * page
        ).map { list ->
            list.map {
                SearchResult(
                    id = it.mbid,
                    name = it.name,
                    desc = it.shortDesc
                )
            }
        }
    }

    override fun getIcon(id: String): Flow<Uri> {
        return fanartTvRepository.getArtistImages(id)
            .map { it.thumbnail }
    }

}