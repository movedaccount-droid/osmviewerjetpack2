package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz

import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ArtistNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.ArtistSearchResponse
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses.ReleaseGroupsByArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// providing the api behind a set of suspend functions, so we have to call these via coroutines

// TODO: also check out https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#5
// see if there's any differences

interface MusicBrainzService {

    @GET("artist")
    suspend fun searchArtists(
        @Query("query") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ArtistSearchResponse

    @GET("artist/{mbid}")
    suspend fun lookupArtist(
        @Path("mbid") mbid: String
    ): ArtistNetwork

    @GET("release-group")
    suspend fun browseReleaseGroupsByArtist(
        @Query("artist") mbid: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ReleaseGroupsByArtistResponse

}