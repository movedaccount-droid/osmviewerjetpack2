package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.PackagingLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.StatusLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TypeLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// for the sake of not spending ten years on this,
// we're ignoring most of the fields here
@Serializable
data class ReleaseNetwork(
    val id: String,
    val type: String?,
    @SerializedName("type-id")
    val typeId: String?,
    val title: String,
    val disambiguation: String,
    val date: String,
    val country: String,
    val barcode: String,
    val media: List<MediaNetwork>,
    val packaging: String?,
    @SerializedName("packaging-id")
    val packagingId: String?,
    val status: String,
    @SerializedName("status-id")
    val statusId: String,
)

fun ReleaseNetwork.toLocal(releaseGroupMbid: String) = ReleaseLocal(
    mbid = id,
    releaseGroupMbid = releaseGroupMbid,
    type = typeId?.let{ TypeLocal(it, type!!) },
    name = title,
    disambiguation = if (disambiguation == "") null else disambiguation,
    date = date,
    country = country,
    barcode = barcode.toLongOrNull(),
    media = media.toLocal()!!, // should always have a tracklisting
    packaging = packagingId?.let { PackagingLocal(packagingId, packaging!!) },
    status = StatusLocal(statusId, status)
)