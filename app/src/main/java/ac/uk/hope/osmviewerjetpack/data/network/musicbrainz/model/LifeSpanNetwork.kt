package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.LifeSpanLocal
import kotlinx.serialization.Serializable

@Serializable
data class LifeSpanNetwork(
    val begin: String?,
    val end: String?,
    val ended: Boolean?,
)

fun LifeSpanNetwork.toLocal() = if (
    begin == null
    && end == null
    && ended == null
    ) null else LifeSpanLocal(
        begin = begin,
        end = end,
        ended = ended ?: (end != null)
    )

fun List<LifeSpanNetwork>.toLocal() = map(LifeSpanNetwork::toLocal)