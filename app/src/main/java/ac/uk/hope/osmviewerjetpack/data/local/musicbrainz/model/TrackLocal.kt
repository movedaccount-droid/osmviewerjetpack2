package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Track
import kotlin.time.Duration

data class TrackLocal(
    val name: String,
    val length: Duration
)

fun TrackLocal.toExternal() = Track(
    name = name,
    length = length
)

fun List<TrackLocal>.toExternal() = map(TrackLocal::toExternal)