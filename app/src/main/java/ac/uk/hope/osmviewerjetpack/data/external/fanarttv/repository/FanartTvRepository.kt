package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface FanartTvRepository {

    fun getArtistImages(
        mbid: String,
    ): Flow<ArtistImages>
}