package ac.uk.hope.osmviewerjetpack.repository.fanarttv

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.network.fanarttv.FanartTvService
import ac.uk.hope.osmviewerjetpack.network.fanarttv.model.ArtistDtoMapper

class FanartTvRepositoryImpl(
    private val service: FanartTvService
): FanartTvRepository {
    override suspend fun getArtistImages(
        mbid: String,
    ): ArtistImages {
        return ArtistDtoMapper().mapFromEntity(
            service.getArtistImages(mbid)
        )
    }
}