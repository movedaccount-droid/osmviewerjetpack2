package ac.uk.hope.osmviewerjetpack.network.fanarttv.model

import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom
import android.net.Uri

class ImageDtoMapper: EntityMapperFrom<ImageDto, Uri> {

    override fun mapFromEntity(imageDto: ImageDto): Uri {
        return Uri.parse(Uri.decode(imageDto.url))
    }

}