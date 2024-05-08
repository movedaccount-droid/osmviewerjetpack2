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
            response.artistBackground?.let { idm.mapFromEntityList(it) },
            response.musicBanner?.let { idm.mapFromEntityList(it) },
            response.hdMusicLogo?.let { idm.mapFromEntityList(it) },
            response.musicLogo?.let { idm.mapFromEntityList(it) },
            response.artistThumb?.let { idm.mapFromEntityList(it) },
        )
    }

}