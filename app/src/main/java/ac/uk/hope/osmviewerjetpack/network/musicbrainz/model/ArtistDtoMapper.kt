package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperTo

class ArtistDtoMapper: EntityMapperFrom<ArtistDto, Artist> {
    override fun mapFromEntity(artistDto: ArtistDto): Artist {
        return Artist(
            artistDto.id,
            artistDto.type,
            artistDto.typeId,
            artistDto.score,
            artistDto.name,
            artistDto.sortName,
            artistDto.country,
            artistDto.area?.let { AreaDtoMapper().mapFromEntity(it) },
            artistDto.beginArea?.let { AreaDtoMapper().mapFromEntity(it) },
            artistDto.disambiguation,
            LifeSpanDtoMapper().mapFromEntity(artistDto.lifeSpan),
            artistDto.tags?.let { TagDtoMapper().mapFromEntityList(it) }
        )
    }

}