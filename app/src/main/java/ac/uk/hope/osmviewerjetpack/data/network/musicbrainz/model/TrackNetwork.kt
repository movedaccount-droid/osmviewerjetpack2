package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TrackLocal
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.milliseconds

@Serializable
data class TrackNetwork (
    val id: String,
    val position: Int,
    val number: String, // position as a string? why
    val length: Int,
    val title: String,
)

fun TrackNetwork.toLocal() = TrackLocal(
    name = title,
    length = length.milliseconds
)

fun List<TrackNetwork>.toLocal() = map(TrackNetwork::toLocal)