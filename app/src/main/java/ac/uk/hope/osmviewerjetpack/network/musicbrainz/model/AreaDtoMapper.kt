package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Area
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperTo

class AreaDtoMapper: EntityMapperTo<AreaDto, Area>, EntityMapperFrom<AreaDto, Area> {
    override fun mapFromEntity(areaDto: AreaDto): Area {
        return Area(
            areaDto.id,
            areaDto.type,
            areaDto.typeId,
            areaDto.name,
            areaDto.sortName,
            LifeSpanDtoMapper().mapFromEntity(areaDto.lifeSpan)
        )
    }

    override fun mapToEntity(area: Area): AreaDto {
        return AreaDto(
            area.id,
            area.type,
            area.typeId,
            area.name,
            area.sortName,
            LifeSpanDtoMapper().mapToEntity(area.lifeSpan)
        )
    }

}