package ac.uk.hope.osmviewerjetpack.network.fanarttv.model

import ac.uk.hope.osmviewerjetpack.domain.fanarttv.model.AlbumImages
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom

class AlbumDtoMapper: EntityMapperFrom<AlbumDto, AlbumImages> {
    override fun mapFromEntity(albumDto: AlbumDto): AlbumImages {
        val idm = ImageDtoMapper()
        return AlbumImages(
            albumDto.albumCover?.let { idm.mapFromEntityList(it) },
            albumDto.cdArt?.let { idm.mapFromEntityList(it) },
        )
    }

}