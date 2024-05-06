package ac.uk.hope.osmviewerjetpack.network.model

import ac.uk.hope.osmviewerjetpack.domain.model.Area
import ac.uk.hope.osmviewerjetpack.domain.model.Artist
import ac.uk.hope.osmviewerjetpack.domain.util.EntityMapper

// seems redundant but preferable for consistency
class ArtistDtoMapper: EntityMapper<ArtistDto, Artist> {
    override fun mapFromEntity(artistDto: ArtistDto): Artist {
        return Artist(
            artistDto.id,
            artistDto.type,
            artistDto.typeId,
            artistDto.score,
            artistDto.name,
            artistDto.sortName,
            artistDto.country,
            AreaDtoMapper().mapFromEntity(artistDto.area),
            artistDto.disambiguation,
            TagDtoMapper().mapFromEntityList(artistDto.tags)
        )
    }

    override fun mapToEntity(artist: Artist): ArtistDto {
        return ArtistDto(
            artist.id,
            artist.type,
            artist.typeId,
            artist.score,
            artist.name,
            artist.sortName,
            artist.country,
            AreaDtoMapper().mapToEntity(artist.area),
            artist.disambiguation,
            TagDtoMapper().mapToEntityList(artist.tags)
        )
    }

    fun mapFromEntityList(artistDtos: List<ArtistDto>): List<Artist> {
        return artistDtos.map { mapFromEntity(it) }
    }

    fun mapToEntityList(artists: List<Artist>): List<ArtistDto> {
        return artists.map { mapToEntity(it) }
    }

}