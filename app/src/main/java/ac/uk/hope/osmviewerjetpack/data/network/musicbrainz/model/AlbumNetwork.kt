package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TypeLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseGroupNetwork(
    val id: String,
    val title: String,
    @SerializedName("primary-type-id")
    val primaryTypeId: String,
    @SerializedName("primary-type")
    val primaryType: String,
    @SerializedName("secondary-type-ids")
    val secondaryTypeIds: List<String>,
    @SerializedName("secondary-types")
    val secondaryTypes: List<String>,
    @SerializedName("first-release-date")
    val firstReleaseDate: String,
    val disambiguation: String,
)

fun ReleaseGroupNetwork.toLocal() = ReleaseGroupLocal(
    mbid = id,
    types = listOf(TypeLocal(mbid = primaryTypeId, name = primaryType))
         + secondaryTypeIds.zip(secondaryTypes).map {
                TypeLocal(mbid = it.first, name = it.second)
           },
    name = title,
    releaseDate = firstReleaseDate,
    disambiguation = disambiguation
)

fun List<ReleaseGroupNetwork>.toLocal() = map(ReleaseGroupNetwork::toLocal)