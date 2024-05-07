package ac.uk.hope.osmviewerjetpack.network.fanarttv

import ac.uk.hope.osmviewerjetpack.network.fanarttv.responses.FanartTvArtistResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// TODO: putting id in a query might be broken. reference says it should just be the url endpoint
interface FanartTvService {
    @GET("music/{id}")
    suspend fun getArtistImages(
        @Path("id") query: String,
    ) : FanartTvArtistResponse
}