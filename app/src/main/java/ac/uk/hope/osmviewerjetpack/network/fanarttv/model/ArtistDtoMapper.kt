package ac.uk.hope.osmviewerjetpack.network.fanarttv.model

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom
import ac.uk.hope.osmviewerjetpack.network.fanarttv.responses.FanartTvArtistResponse
import java.net.URL

// again FATVAR is just our ArtistDto, check its class

class ArtistDtoMapper: EntityMapperFrom<FanartTvArtistResponse, ArtistImages> {
    override fun mapFromEntity(response: FanartTvArtistResponse): ArtistImages {
        val idm = ImageDtoMapper()
        return ArtistImages(
            idm.mapFromEntityList(response.artistBackground),
            idm.mapFromEntityList(response.musicBanner),
            idm.mapFromEntityList(response.hdMusicLogo),
            idm.mapFromEntityList(response.musicLogo),
            idm.mapFromEntityList(response.artistThumb),
        )
    }

}