package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses

import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BrowseReleaseGroupsResponse (
    @SerializedName("release-group-offset")
    val releaseGroupOffset: Int,
    @SerializedName("release-group-count")
    val releaseGroupCount: Int,
    @SerializedName("release-groups")
    val releaseGroups: List<ReleaseGroupNetwork>
)

fun BrowseReleaseGroupsResponse.toLocal() = releaseGroups.toLocal()