package ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LifeSpan(
    val begin: String?,
    val end: String?,
    val ended: Boolean?,
) : Parcelable