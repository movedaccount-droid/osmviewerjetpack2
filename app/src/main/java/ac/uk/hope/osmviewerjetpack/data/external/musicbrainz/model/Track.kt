package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

import kotlin.time.Duration

data class Track(
    val name: String,
    val length: Duration
)