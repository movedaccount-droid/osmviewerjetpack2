package ac.uk.hope.osmviewerjetpack.network.service

import ac.uk.hope.osmviewerjetpack.network.model.NominatimFreeformResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

// providing the api behind a set of suspend functions, so we have to call these via coroutines

// TODO: also check out https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#5
// see if there's any differences
// TODO: do we need a token? not so far, but maybe "officially"

interface NominatimRetrofitService {
    @GET("search")
    suspend fun freeform(
        @Query("q") query: String
    ) : NominatimFreeformResponseModel

    @GET("search")
    suspend fun structured(
        @Query("amenity") amenity: String?,
        @Query("amenity") street: String?,
        @Query("amenity") city: String?,
        @Query("amenity") county: String?,
        @Query("amenity") state: String?,
        @Query("amenity") country: String?,
        @Query("amenity") postalcode: String?
    )
}