package ac.uk.hope.osmviewerjetpack.displayables.search.album

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.displayables.search.SearchResult
import ac.uk.hope.osmviewerjetpack.displayables.search.SearchViewSearcher
import android.net.Uri
import kotlinx.coroutines.flow.Flow

class AlbumSearchViewSearcher(
    private val musicBrainzRepository: MusicBrainzRepository,
    private val fanartTvRepository: FanartTvRepository
): SearchViewSearcher {

    override val pageSize = 15
    lateinit var artistMbid: String

    override fun getPage(page: Int): Flow<List<SearchResult>> {
        TODO("converting to paging three. ougghhhhh")
//        return musicBrainzRepository.getArtistAlbums(
//            mbid = artistMbid,
//            limit = pageSize,
//            offset = pageSize * page
//        ).map { list ->
//            list.map {
//                SearchResult(
//                    id = it.mbid,
//                    name = it.name,
//                    desc = it.shortDesc
//                )
//            }
//        }
    }

    override fun getIcon(id: String): Flow<Uri> {
        return fanartTvRepository.getAlbumImages(id)
            .map { it.thumbnail }
    }

}