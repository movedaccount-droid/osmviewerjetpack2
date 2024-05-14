package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LifeSpanLocal(
    val begin: String?,
    val end: String?,
    val ended: Boolean?,
) : Parcelable