package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom

class TagDtoMapper: EntityMapperFrom<TagDto, String> {
    override fun mapFromEntity(tagDto: TagDto): String {
        return tagDto.name
    }

}