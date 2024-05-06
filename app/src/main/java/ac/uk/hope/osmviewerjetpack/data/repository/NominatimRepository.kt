package ac.uk.hope.osmviewerjetpack.data.repository

import ac.uk.hope.osmviewerjetpack.data.model.Node

// TODO: defining an interface seems unnecessary. why are we doing this?

interface NominatimRepository {
    suspend fun freeform(
        query: String
    ) : List<Node>

    suspend fun structured(
        amenity: String?,
        street: String?,
        city: String?,
        county: String?,
        state: String?,
        country: String?,
        postalcode: String?
    ) : List<Node>
}