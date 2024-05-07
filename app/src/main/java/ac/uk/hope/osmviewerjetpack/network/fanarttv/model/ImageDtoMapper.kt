package ac.uk.hope.osmviewerjetpack.network.fanarttv.model

import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom
import java.net.URL

class ImageDtoMapper: EntityMapperFrom<ImageDto, URL> {
    override fun mapFromEntity(imageDto: ImageDto): URL {
        return URL(imageDto.url)
    }

}