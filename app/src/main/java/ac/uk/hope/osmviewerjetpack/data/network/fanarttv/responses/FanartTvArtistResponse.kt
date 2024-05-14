package ac.uk.hope.osmviewerjetpack.data.network.fanarttv.responses

import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.AlbumImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.responses.FanartTvArtistResponseLocal
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.model.AlbumImagesNetwork
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.model.ImageNetwork
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.model.toLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FanartTvArtistResponse (
    val name: String,
    @SerializedName("mbid_id")
    val mbidId: String,
    @SerializedName("artistbackground")
    val artistBackground: List<ImageNetwork>?,
    @SerializedName("artistthumb")
    val artistThumb: List<ImageNetwork>?,
    @SerializedName("musiclogo")
    val musicLogo: List<ImageNetwork>?,
    @SerializedName("hdmusiclogo")
    val hdMusicLogo: List<ImageNetwork>?,
    val albums: Map<String, AlbumImagesNetwork>?,
    @SerializedName("musicbanner")
    val musicBanner: List<ImageNetwork>?,
)

fun FanartTvArtistResponse.toLocal() =
    FanartTvArtistResponseLocal(
        artist = ArtistImagesLocal(
            mbid = mbidId,
            backgrounds = artistBackground?.toLocal() ?: listOf(),
            banners = musicBanner?.toLocal() ?: listOf(),
            hdLogos = hdMusicLogo?.toLocal() ?: listOf(),
            logos = musicLogo?.toLocal() ?: listOf(),
            thumbnails = artistThumb?.toLocal() ?: listOf()
        ),
        albums = albums?.map { album ->
            AlbumImagesLocal(
                mbid = album.key,
                covers = album.value.albumCover?.toLocal() ?: listOf(),
                cds = album.value.cdArt?.toLocal() ?: listOf(),
            )
        }
    )