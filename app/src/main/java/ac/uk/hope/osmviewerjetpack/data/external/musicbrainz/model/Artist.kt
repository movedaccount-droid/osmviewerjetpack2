package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

// TODO: move description retrieval here
data class Artist(
    val mbid: String,
    val type: String?,
    val name: String,
    val sortName: String,
    val country: String?,
    val area: Area?,
    val beginArea: Area?,
    val disambiguation: String?,
    val lifeSpan: LifeSpan?,
    val tags: Map<String, Int>?
)