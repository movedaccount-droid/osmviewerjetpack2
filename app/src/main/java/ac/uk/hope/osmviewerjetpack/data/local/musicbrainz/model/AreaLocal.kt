package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AreaLocal(
    val id: String,
    val type: String,
    val typeId: String,
    val name: String,
    val sortName: String,
    val lifeSpan: ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.LifeSpanLocal
) : Parcelable