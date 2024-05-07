package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.LifeSpan
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperFrom
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapperTo

// seems redundant but preferable for consistency
class LifeSpanDtoMapper: EntityMapperTo<LifeSpanDto, LifeSpan>,
    EntityMapperFrom<LifeSpanDto, LifeSpan> {
    override fun mapFromEntity(lifeSpanDto: LifeSpanDto): LifeSpan {
        return LifeSpan(
            lifeSpanDto.begin,
            lifeSpanDto.end,
            lifeSpanDto.ended
        )
    }

    override fun mapToEntity(lifeSpan: LifeSpan): LifeSpanDto {
        return LifeSpanDto(
            lifeSpan.begin,
            lifeSpan.end,
            lifeSpan.ended
        )
    }

}