package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.responses

import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ArtistNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.toLocal
import kotlinx.serialization.Serializable

// model for the api response, which we will then pass to the repository mappers for parsing

@Serializable
data class ArtistSearchResponse (
    val created: String,
    val count: Int,
    val offset: Int,
    val artists: List<ArtistNetwork>
)

fun ArtistSearchResponse.toLocal() = artists.toLocal()