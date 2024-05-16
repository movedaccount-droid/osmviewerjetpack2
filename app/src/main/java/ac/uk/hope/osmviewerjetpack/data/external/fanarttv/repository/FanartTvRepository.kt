package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.AlbumImages
import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import kotlinx.coroutines.flow.Flow

interface FanartTvRepository {

    fun getArtistImages(
        mbid: String,
    ): Flow<ArtistImages>

    fun getAlbumImages(
        mbid: String,
    ): Flow<AlbumImages>
}