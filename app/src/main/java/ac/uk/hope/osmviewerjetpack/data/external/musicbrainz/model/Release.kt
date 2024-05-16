package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

class Release (
    val type: String?,
    val name: String,
    val disambiguation: String?,
    val date: String,
    val country: String,
    val barcode: Long?,
    val tracks: List<Track>,
    val format: String,
    val packaging: String?,
    val status: String,
)