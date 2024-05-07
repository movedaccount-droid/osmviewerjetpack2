package ac.uk.hope.osmviewerjetpack.network.fanarttv.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class AlbumDto(
    @SerializedName("albumcover")
    val albumCover: List<ImageDto>?,
    @SerializedName("cdart")
    val cdArt: List<ImageDto>?,
)