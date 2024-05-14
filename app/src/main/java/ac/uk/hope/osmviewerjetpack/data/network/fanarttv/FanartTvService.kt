package ac.uk.hope.osmviewerjetpack.data.network.fanarttv

import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.responses.FanartTvArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FanartTvService {
    @GET("music/{id}")
    suspend fun getArtistImages(
        @Path("id") query: String,
    ) : FanartTvArtistResponse
}