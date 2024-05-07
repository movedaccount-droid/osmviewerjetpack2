package ac.uk.hope.osmviewerjetpack.network.musicbrainz.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: String,
    val type: String?,
    @SerializedName("type-id")
    val typeId: String?,
    val score: Int,
    val name: String,
    @SerializedName("sort-name")
    val sortName: String,
    val country: String?,
    val area: AreaDto?,
    @SerializedName("begin-area")
    val beginArea: AreaDto?,
    val disambiguation: String?,
    @SerializedName("life-span")
    val lifeSpan: LifeSpanDto,
    val tags: List<TagDto>?,
)