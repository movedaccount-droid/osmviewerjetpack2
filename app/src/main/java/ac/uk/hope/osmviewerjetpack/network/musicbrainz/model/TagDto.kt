package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val count: Int,
    val name: String
)