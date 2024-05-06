package ac.uk.hope.osmviewerjetpack.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: String,
    val type: String,
    @SerialName("type-id")
    val typeId: String,
    val score: Int,
    val name: String,
    @SerialName("sort-name")
    val sortName: String,
    val country: String,
    val area: AreaDto,
    val disambiguation: String,
    // TODO: do we need the life-span var?
    val tags: List<TagDto>
)