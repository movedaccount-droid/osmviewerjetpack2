package ac.uk.hope.osmviewerjetpack.data.network.fanarttv

import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.model.AlbumImagesNetwork
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.responses.FanartTvArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FanartTvService {
    @GET("music/{mbid}")
    suspend fun getArtistImages(
        @Path("mbid") mbid: String,
    ): FanartTvArtistResponse

    @GET("music/albums/{mbid}")
    suspend fun getAlbumImages(
        @Path("mbid") mbid: String,
    ): AlbumImagesNetwork
}