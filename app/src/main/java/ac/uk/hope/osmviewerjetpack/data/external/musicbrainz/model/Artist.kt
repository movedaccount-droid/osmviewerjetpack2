package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

// TODO: move description retrieval here
data class Artist(
    val mbid: String,
    val name: String,
    val sortName: String,
    val type: String? = null,
    val country: String? = null,
    val area: Area? = null,
    val beginArea: Area? = null,
    val disambiguation: String? = null,
    val lifeSpan: LifeSpan? = null,
    val tags: Map<String, Int> = mapOf(),
    val followed: Boolean = false
) {
    val shortDesc
        get() = disambiguation
            ?: type
            ?: tags.keys.joinToString(", ")
            ?: beginArea?.name

    val activeText
        get() = lifeSpan?.let { lifeSpan ->
            val yat = yearsActiveText
            val ended = lifeSpan.ended?.let { if (it) "Ended" else "Active" }
            if (ended != null && yat != null) {
                "$ended ($yat)"
            } else ended ?: yat
        }

    private val yearsActiveText
        get() = lifeSpan?.let {
            if (it.begin != null && it.end != null) {
                "${it.begin} - ${it.end}"
            } else if (it.begin != null) {
                "from ${it.begin}"
            } else if (it.end != null) {
                "til ${it.end}"
            } else {
                null
            }
        }

    val sortedTags
        get() = tags.entries.sortedByDescending { it.value }.map { it.key }
}