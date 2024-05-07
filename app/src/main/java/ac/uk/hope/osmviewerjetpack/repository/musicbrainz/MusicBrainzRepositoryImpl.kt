package ac.uk.hope.osmviewerjetpack.repository.musicbrainz

import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.network.musicbrainz.model.ArtistDtoMapper

// repositories officially "own" our mapper functions and take the network/services
// to pass through them, responding with our final model

class MusicBrainzRepositoryImpl(
    private val musicBrainzService: MusicBrainzService
): MusicBrainzRepository {
    override suspend fun searchArtistsName(
        query: String,
        limit: Int,
        offset: Int
    ): List<Artist> {
        return ArtistDtoMapper().mapFromEntityList(
            musicBrainzService.searchArtists(
                "artist:$query",
                limit,
                offset
            ).artists
        )
    }
}