package ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model

import java.util.SortedMap

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
    val tags: Map<String, Int>
) {
    val shortDesc
        get() = disambiguation
            ?: type
            ?: tags.keys.joinToString(", ")
            ?: area?.name
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