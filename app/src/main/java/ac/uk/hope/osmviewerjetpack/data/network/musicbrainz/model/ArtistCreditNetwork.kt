package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistCreditNetwork(
    val name: String,
    val artist: ArtistNetwork,
    @SerializedName("joinphrase")
    val joinPhrase: String,
)

fun ArtistCreditNetwork.toLocal() = artist.toLocal()

fun List<ArtistCreditNetwork>.toLocal() = map(ArtistCreditNetwork::toLocal)