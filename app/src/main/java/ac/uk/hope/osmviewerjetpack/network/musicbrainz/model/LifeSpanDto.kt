package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LifeSpanDto(
    val begin: String?,
    val end: String?,
    val ended: Boolean?,
)