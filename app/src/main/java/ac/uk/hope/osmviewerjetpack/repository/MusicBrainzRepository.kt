package ac.uk.hope.osmviewerjetpack.repository

import ac.uk.hope.osmviewerjetpack.domain.model.Artist

// we define our repositories as an interface, so that we can easily swap them out
// for testing. if we get the chance

interface MusicBrainzRepository {
    suspend  fun searchArtists(
        query: String,
        limit: Int?,
        offset: Int?
    ): List<Artist>
}