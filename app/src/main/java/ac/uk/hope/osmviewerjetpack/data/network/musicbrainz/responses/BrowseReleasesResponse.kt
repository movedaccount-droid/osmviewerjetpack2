package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses

import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseNetwork
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BrowseReleasesResponse(
    @SerializedName("release-offset")
    val releaseOffset: Int,
    @SerializedName("release-count")
    val releaseCount: Int,
    @SerializedName("releases")
    val releases: List<ReleaseNetwork>
)