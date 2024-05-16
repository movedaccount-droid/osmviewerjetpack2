package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.LifeSpan

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

fun List<LifeSpanLocal>.toExternal() = map(LifeSpanLocal::toExternal)