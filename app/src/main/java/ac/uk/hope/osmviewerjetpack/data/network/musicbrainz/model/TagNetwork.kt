package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import kotlinx.serialization.Serializable

// this doesn't have a local model because tags don't have a central id of any kind,
// but we need to have a network object anyway to catch them before embedding them
// i miss serde...

@Serializable
data class TagNetwork(
    val name: String,
    val count: Int
)

fun List<TagNetwork>.toLocal() = associateBy({it.name}, {it.count})