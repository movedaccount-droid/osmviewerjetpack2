package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import kotlinx.serialization.Serializable

@Serializable
data class TagNetwork(
    val count: Int,
    val name: String
)

// TODO: we probably should store the count locally, even if we're not going to use it.
// if we want to abstract information away, we should handle that surface-layer.
fun TagNetwork.toLocal() = name

fun List<TagNetwork>.toLocal() = map(TagNetwork::toLocal)