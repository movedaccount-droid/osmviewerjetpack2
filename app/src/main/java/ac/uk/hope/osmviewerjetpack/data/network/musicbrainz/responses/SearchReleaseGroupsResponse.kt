package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses

import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SearchReleaseGroupsResponse(
    val created: String,
    val count: Int,
    val offset: Int,
    @SerializedName("release-groups")
    val releaseGroups: List<ReleaseGroupNetwork>
)

fun SearchReleaseGroupsResponse.toLocal() = releaseGroups.toLocal()