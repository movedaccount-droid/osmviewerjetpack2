package ac.uk.hope.osmviewerjetpack.network.model

import ac.uk.hope.osmviewerjetpack.domain.model.Artist
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapper

class TagDtoMapper: EntityMapper<TagDto, String> {
    override fun mapFromEntity(tagDto: TagDto): String {
        return tagDto.name
    }

    // TODO: this might be a bad idea. what is this number actually for?
    override fun mapToEntity(name: String): TagDto {
        return TagDto(1, name)
    }

    fun mapFromEntityList(tagDtos: List<TagDto>): List<String> {
        return tagDtos.map { mapFromEntity(it) }
    }

    fun mapToEntityList(tags: List<String>): List<TagDto> {
        return tags.map { mapToEntity(it) }
    }
    
}