package ac.uk.hope.osmviewerjetpack.data.repository

import ac.uk.hope.osmviewerjetpack.data.model.Node

// we define our repositories as an interface, so that we can easily swap them out
// for testing. if we get the chance

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