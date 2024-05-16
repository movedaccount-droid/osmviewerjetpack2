package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FormatLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.MediaLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TrackLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MediaNetwork(
    val format: String,
    @SerializedName("format-id")
    val formatId: String,
    val tracks: List<TrackNetwork>
)

fun MediaNetwork.toLocal() = MediaLocal(
    format = FormatLocal(format, formatId),
    tracks = tracks.toLocal()
)


// we combine all disks etc. in a release into a single listing for ease
fun List<MediaNetwork>.toLocal(): MediaLocal? {
    if (isEmpty()) return null
    val format = first().let { FormatLocal(it.format, it.formatId) }
    val tracks = fold<MediaNetwork, List<TrackLocal>>(listOf()) { acc, mediaNetwork ->
        acc + mediaNetwork.tracks.toLocal()
    }
    return MediaLocal(
        format = format,
        tracks = tracks
    )
}