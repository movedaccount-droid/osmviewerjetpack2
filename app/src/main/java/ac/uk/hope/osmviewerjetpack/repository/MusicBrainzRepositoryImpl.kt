package ac.uk.hope.osmviewerjetpack.repository

import ac.uk.hope.osmviewerjetpack.domain.model.Artist
import ac.uk.hope.osmviewerjetpack.network.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.network.model.ArtistDtoMapper

// repositories officially "own" our mapper functions, which we don't have
// and take the network/services to pass through them,
// responding with our final model

// TODO: we need to dependency inject these
class MusicBrainzRepositoryImpl(
    private val musicBrainzService: MusicBrainzService
): MusicBrainzRepository {
    override suspend fun searchArtists(query: String, limit: Int?, offset: Int?): List<Artist> {
        return ArtistDtoMapper().mapFromEntityList(
            musicBrainzService.searchArtists(query, limit, offset).artists
        )
    }
}