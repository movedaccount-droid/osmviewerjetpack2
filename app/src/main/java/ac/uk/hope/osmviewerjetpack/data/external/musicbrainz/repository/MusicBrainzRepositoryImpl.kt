package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal

// repositories officially "own" our mapper functions and take the network/services
// to pass through them, responding with our final model

class MusicBrainzRepositoryImpl(
    private val musicBrainzService: MusicBrainzService
): MusicBrainzRepository {

    private val rateLimiter = RateLimiter(1000)

    override suspend fun searchArtistsName(
        query: String,
        limit: Int,
        offset: Int
    // TODO: this should be an external
    ): List<ArtistLocal> {
        rateLimiter.await()
        return musicBrainzService.searchArtists(
            "artist:$query",
            limit,
            offset
        ).artists.toLocal()
    }
}