package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import kotlinx.coroutines.flow.Flow

// we define our repositories as an interface, so that we can easily swap them out
// for testing. if we get the chance

interface MusicBrainzRepository {
    fun searchArtistsName(
        query: String,
        limit: Int = 10,
        offset: Int = 0
    ): Flow<List<Artist>>

    fun getArtist(mbid: String): Flow<Artist>
}