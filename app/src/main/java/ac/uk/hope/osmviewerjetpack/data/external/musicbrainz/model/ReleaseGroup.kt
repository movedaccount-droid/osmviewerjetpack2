package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

data class ReleaseGroup(
    val mbid: String,
    val artistMbids: List<String>,
    val types: List<String>,
    val name: String,
    val releaseDate: String,
    val disambiguation: String?
) {
    val shortDesc
        get() = disambiguation
            ?: types.getOrNull(0)?.let { "$it, $releaseDate" }
            ?: releaseDate
}