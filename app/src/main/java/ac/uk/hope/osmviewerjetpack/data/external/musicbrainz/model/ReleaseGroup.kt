package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

data class ReleaseGroup(
    val types: List<String>,
    val name: String,
    val releaseDate: String,
    val disambiguation: String
)