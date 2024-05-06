package ac.uk.hope.osmviewerjetpack.network.model

import ac.uk.hope.osmviewerjetpack.domain.model.Area
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapper

// seems redundant but preferable for consistency
class AreaDtoMapper: EntityMapper<AreaDto, Area> {
    override fun mapFromEntity(areaDto: AreaDto): Area {
        return Area(
            areaDto.id,
            areaDto.type,
            areaDto.typeId,
            areaDto.name
        )
    }

    override fun mapToEntity(area: Area): AreaDto {
        return AreaDto(
            area.id,
            area.type,
            area.typeId,
            area.name
        )
    }

    fun mapFromEntityList(areaDtos: List<AreaDto>): List<Area> {
        return areaDtos.map { mapFromEntity(it) }
    }

    fun mapToEntityList(areas: List<Area>): List<AreaDto> {
        return areas.map { mapToEntity(it) }
    }

}