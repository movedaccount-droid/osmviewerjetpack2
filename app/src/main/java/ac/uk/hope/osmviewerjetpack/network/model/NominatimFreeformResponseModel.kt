package ac.uk.hope.osmviewerjetpack.network.model

import ac.uk.hope.osmviewerjetpack.model.Node
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// model for the entire freeform search response,
// which we split into its relevant items
// TODO: fill this out!!

@Serializable
class NominatimFreeformResponseModel (
    @SerialName("some")
    var some: String,
    var other: List<Node>
)