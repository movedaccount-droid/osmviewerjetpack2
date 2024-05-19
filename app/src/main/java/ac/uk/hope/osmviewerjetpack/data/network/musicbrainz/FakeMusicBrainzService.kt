package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ArtistNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.BrowseReleaseGroupsResponse
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.BrowseReleasesResponse
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.SearchArtistsResponse
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.SearchReleaseGroupsResponse
import ac.uk.hope.osmviewerjetpack.displayables.Release
import ac.uk.hope.osmviewerjetpack.util.toDateString
import androidx.core.math.MathUtils.clamp
import java.util.Calendar

// search functions return based on name 

class FakeMusicBrainzService(
    private val searchReleaseGroupEntries: List<ReleaseGroupNetwork> = listOf(),
    private val lookupArtistEntry: ArtistNetwork? = null,
): MusicBrainzService {
    override suspend fun searchArtists(
        query: String,
        limit: Int,
        offset: Int
    ): SearchArtistsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchReleaseGroups(
        query: String,
        limit: Int,
        offset: Int,
        inc: String,
        showType: String
    ): SearchReleaseGroupsResponse {
        val subList = searchReleaseGroupEntries.subListClamped(offset, offset + limit)
        return SearchReleaseGroupsResponse(
            created = Calendar.getInstance().toDateString(),
            count = searchReleaseGroupEntries.size,
            offset = offset,
            releaseGroups = subList
        )
    }

    override suspend fun lookupArtist(mbid: String): ArtistNetwork {
        if (lookupArtistEntry != null && lookupArtistEntry.id == mbid) {
            return lookupArtistEntry
        } else {
            error("did not have artist")
        }
    }

    override suspend fun browseReleaseGroups(
        mbid: String,
        limit: Int,
        offset: Int,
        inc: String,
        showType: String
    ): BrowseReleaseGroupsResponse {
        TODO("Not yet implemented")
    }

    override suspend fun browseReleases(
        mbid: String,
        limit: Int,
        offset: Int,
        inc: String
    ): BrowseReleasesResponse {
        TODO("Not yet implemented")
    }
}

private data class Indices(val start: Int, val end: Int)

private fun <T> List<T>.subListClamped(startIndex: Int, endIndex: Int) = subList(
    clamp(startIndex, 0, this.size),
    clamp(endIndex, 0, this.size)
)