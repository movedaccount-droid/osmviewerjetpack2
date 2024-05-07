package ac.uk.hope.osmviewerjetpack.network.musicbrainz.responses

import ac.uk.hope.osmviewerjetpack.network.musicbrainz.model.ArtistDto
import kotlinx.serialization.Serializable

// model for the api response, which we will then pass to the repository mappers for parsing

@Serializable
data class MusicBrainzArtistSearchResponse (
    val created: String,
    val count: Int,
    val offset: Int,
    val artists: List<ArtistDto>
)