package ac.uk.hope.osmviewerjetpack.data.service.model

import ac.uk.hope.osmviewerjetpack.data.model.Node
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// model for the entire freeform search response,
// which we split into its relevant items
// TODO: fill this out!!

@Serializable
data class NominatimSearchResponse (
    @SerialName("some")
    var some: String,
    var other: List<Node>
)