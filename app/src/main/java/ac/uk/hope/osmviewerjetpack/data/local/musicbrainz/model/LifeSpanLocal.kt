package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.LifeSpan
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.LifeSpanNetwork

// this is a very silly class and should have just been embedded network -> local

data class LifeSpanLocal(
    val begin: String?,
    val end: String?,
    val ended: Boolean?,
)

fun LifeSpanLocal.toExternal() = LifeSpan(
    begin = begin,
    end = end,
    ended = ended
)

fun LifeSpanLocal.toNetwork() = LifeSpanNetwork(
    begin = begin,
    end = end,
    ended = ended,
)

fun List<LifeSpanLocal>.toExternal() = map(LifeSpanLocal::toExternal)