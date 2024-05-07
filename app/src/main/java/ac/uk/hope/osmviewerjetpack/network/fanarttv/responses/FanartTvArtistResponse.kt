package ac.uk.hope.osmviewerjetpack.network.fanarttv.responses

import ac.uk.hope.osmviewerjetpack.network.fanarttv.model.AlbumDto
import ac.uk.hope.osmviewerjetpack.network.fanarttv.model.ImageDto
import ac.uk.hope.osmviewerjetpack.network.musicbrainz.model.ArtistDto
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// this is unfortunately what should be our ArtistDto. i'm unsure if there's some way to put this
// out to an external class a la serde, but for now we just keep it this way

@Serializable
data class FanartTvArtistResponse (
    val name: String,
    @SerializedName("mbid_id")
    val mbidId: String,
    @SerializedName("artistbackground")
    val artistBackground: List<ImageDto>,
    @SerializedName("artistthumb")
    val artistThumb: List<ImageDto>,
    @SerializedName("musiclogo")
    val musicLogo: List<ImageDto>,
    @SerializedName("hdmusiclogo")
    val hdMusicLogo: List<ImageDto>,
    val albums: Map<String, AlbumDto>,
    @SerializedName("musicbanner")
    val musicBanner: List<ImageDto>,
)