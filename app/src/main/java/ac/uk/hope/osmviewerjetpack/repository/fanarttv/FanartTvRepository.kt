package ac.uk.hope.osmviewerjetpack.repository.fanarttv

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.ArtistImages

interface FanartTvRepository {
    suspend fun getArtistImages(
        mbid: String,
    ): ArtistImages
}