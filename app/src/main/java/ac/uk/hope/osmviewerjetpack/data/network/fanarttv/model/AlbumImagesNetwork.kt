package ac.uk.hope.osmviewerjetpack.data.network.fanarttv.model

import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.AlbumImagesLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class AlbumImagesNetwork(
    val mbid: String,
    @SerializedName("albumcover")
    val albumCover: List<ImageNetwork>?,
    @SerializedName("cdart")
    val cdArt: List<ImageNetwork>?,
)

fun AlbumImagesNetwork.toLocal() =
    AlbumImagesLocal(
        mbid = mbid,
        covers = albumCover?.toLocal() ?: listOf(),
        cds = cdArt?.toLocal() ?: listOf()
    )

fun List<AlbumImagesNetwork>.toLocal() = map(AlbumImagesNetwork::toLocal)