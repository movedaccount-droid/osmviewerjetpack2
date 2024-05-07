package ac.uk.hope.osmviewerjetpack.domain.musicbrainz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Area(
    val id: String,
    val type: String,
    val typeId: String,
    val name: String,
    val sortName: String,
    val lifeSpan: LifeSpan
) : Parcelable