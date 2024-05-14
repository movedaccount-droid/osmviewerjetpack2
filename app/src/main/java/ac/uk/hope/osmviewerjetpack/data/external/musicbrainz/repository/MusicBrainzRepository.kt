package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

// we define our repositories as an interface, so that we can easily swap them out
// for testing. if we get the chance

interface MusicBrainzRepository {
    suspend fun searchArtistsName(
        query: String,
        limit: Int = 10,
        offset: Int = 0
    ): List<ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal>
}