package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class AreaDto(
    val id: String,
    val type: String,
    @SerializedName("type-id")
    val typeId: String,
    val name: String,
    @SerializedName("sort-name")
    val sortName: String,
    @SerializedName("life-span")
    val lifeSpan: LifeSpanDto
)