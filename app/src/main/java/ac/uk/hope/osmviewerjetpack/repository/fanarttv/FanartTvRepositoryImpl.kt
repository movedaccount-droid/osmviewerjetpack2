package ac.uk.hope.osmviewerjetpack.repository.fanarttv

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.network.fanarttv.FanartTvService
import ac.uk.hope.osmviewerjetpack.network.fanarttv.model.ArtistDtoMapper
import ac.uk.hope.osmviewerjetpack.repository.util.RateLimiter

class FanartTvRepositoryImpl(
    private val service: FanartTvService
): FanartTvRepository {

    // TODO: for now we set this reaally high until we stop hammering it with our ui
    private val rateLimiter = RateLimiter(1000)
    override suspend fun getArtistImages(
        mbid: String,
    ): ArtistImages {
        rateLimiter.await()
        return ArtistDtoMapper().mapFromEntity(
            service.getArtistImages(mbid)
        )
    }
}