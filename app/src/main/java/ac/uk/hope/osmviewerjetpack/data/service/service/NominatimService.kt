package ac.uk.hope.osmviewerjetpack.data.service.service

import ac.uk.hope.osmviewerjetpack.data.service.model.NominatimSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

// providing the api behind a set of suspend functions, so we have to call these via coroutines

// TODO: also check out https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#5
// see if there's any differences
// TODO: do we need a token? not so far, but maybe "officially"

interface NominatimService {
    @GET("search")
    suspend fun freeform(
        @Query("q") query: String
    ) : NominatimSearchResponse

    @GET("search")
    suspend fun structured(
        @Query("amenity") amenity: String?,
        @Query("street") street: String?,
        @Query("city") city: String?,
        @Query("county") county: String?,
        @Query("state") state: String?,
        @Query("country") country: String?,
        @Query("postalcode") postalcode: String?
    ) : NominatimSearchResponse
}