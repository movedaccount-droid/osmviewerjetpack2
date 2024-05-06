package ac.uk.hope.osmviewerjetpack.data.repository

import ac.uk.hope.osmviewerjetpack.data.model.Node
import ac.uk.hope.osmviewerjetpack.data.service.service.NominatimService

// repositories officially "own" our mapper functions, which we don't have
// and take the network/services to pass through them,
// responding with our final model

// TODO: we need to dependency inject these
class NominatimRepositoryImpl(
    private val nominatimService: NominatimService
): NominatimRepository {
    override suspend fun freeform(query: String): List<Node> {
        return nominatimService.freeform(query).other;
    }

    override suspend fun structured(
        amenity: String?,
        street: String?,
        city: String?,
        county: String?,
        state: String?,
        country: String?,
        postalcode: String?
    ): List<Node> {
        return nominatimService.structured(
            amenity,
            street,
            city,
            county,
            state,
            country,
            postalcode
        ).other;
    }

}