package ac.uk.hope.osmviewerjetpack.repository.musicbrainz

import ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model.Artist

// we define our repositories as an interface, so that we can easily swap them out
// for testing. if we get the chance

interface MusicBrainzRepository {
    suspend fun searchArtistsName(
        query: String,
        limit: Int = 10,
        offset: Int = 0
    ): List<Artist>
}