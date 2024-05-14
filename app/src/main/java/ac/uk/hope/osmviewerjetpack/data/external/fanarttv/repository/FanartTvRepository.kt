package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import kotlinx.coroutines.flow.Flow

interface FanartTvRepository {
    suspend fun getArtistImages(
        mbid: String,
    ): Flow<ArtistImages>
}