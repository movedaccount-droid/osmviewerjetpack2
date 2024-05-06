package ac.uk.hope.osmviewerjetpack.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AreaDto(
    val id: String,
    val type: String,
    @SerialName("type-id")
    val typeId: String,
    val name: String,
)