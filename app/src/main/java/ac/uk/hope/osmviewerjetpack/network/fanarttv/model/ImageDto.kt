package ac.uk.hope.osmviewerjetpack.network.fanarttv.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class ImageDto(
    val id: Int,
    val url: String,
    val likes: Int,
    val disc: Int,
    val size: Int,
)