package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Release
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import kotlinx.coroutines.flow.Flow

// we define our repositories as an interface, so that we can easily swap them out
// for testing. if we get the chance

interface MusicBrainzRepository {

    fun getArtist(mbid: String): Flow<Artist>

    fun getArtists(mbids: List<String>): Flow<List<Artist>>

    fun getFollowedArtists(): Flow<List<Artist>>

    suspend fun followArtist(mbid: String)

    suspend fun unfollowArtist(mbid: String)

    fun isFollowed(mbid: String): Flow<Boolean>

    suspend fun updateFollowedCaches(): List<ReleaseGroup>

    fun getReleaseWithReleaseGroup(releaseGroupMbid: String): Flow<Pair<ReleaseGroup, Release>>

    suspend fun addNotification(releaseGroupMbid: String)

    suspend fun prune()

}