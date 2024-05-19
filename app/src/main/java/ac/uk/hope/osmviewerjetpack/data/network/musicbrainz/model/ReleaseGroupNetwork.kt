package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TypeLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseGroupNetwork(
    val id: String,
    val title: String,
    @SerializedName("first-release-date")
    val firstReleaseDate: String,
    @SerializedName("primary-type-id")
    val primaryTypeId: String? = null,
    @SerializedName("primary-type")
    val primaryType: String? = null,
    @SerializedName("secondary-type-ids")
    val secondaryTypeIds: List<String> = listOf(),
    @SerializedName("secondary-types")
    val secondaryTypes: List<String> = listOf(),
    @SerializedName("artist-credit")
    val artistCredit: List<ArtistCreditNetwork> = listOf(),
    val disambiguation: String = "",
)

fun ReleaseGroupNetwork.toLocal() = ReleaseGroupLocal(
    mbid = id,
    artistMbids = artistCredit.toLocal().map { it.artist.mbid },
    types = primaryTypeId?.let { primaryTypeId ->
        listOf(
            TypeLocal(
                mbid = primaryTypeId,
                name = primaryType!!
            )
        ) + secondaryTypeIds.zip(secondaryTypes).map {
            TypeLocal(mbid = it.first, name = it.second)
        }
    } ?: listOf(),
    name = title,
    releaseDate = firstReleaseDate,
    disambiguation = if (disambiguation == "") null else disambiguation
)

fun List<ReleaseGroupNetwork>.toLocal() = map(ReleaseGroupNetwork::toLocal)