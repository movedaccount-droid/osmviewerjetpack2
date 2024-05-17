package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Release
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupWithReleaseLocal
import kotlinx.coroutines.flow.Flow

// we define our repositories as an interface, so that we can easily swap them out
// for testing. if we get the chance

interface MusicBrainzRepository {

    fun getArtist(mbid: String): Flow<Artist>

    fun getFollowedArtists(): Flow<List<Artist>>

    fun isArtistFollowed(mbid: String): Flow<Boolean>

    fun getReleaseWithReleaseGroup(releaseGroupMbid: String): Flow<Pair<ReleaseGroup, Release>>

}