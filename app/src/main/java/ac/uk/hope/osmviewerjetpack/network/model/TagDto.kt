package ac.uk.hope.osmviewerjetpack.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val count: Int,
    val name: String
)