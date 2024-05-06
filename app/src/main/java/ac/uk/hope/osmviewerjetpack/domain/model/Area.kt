package ac.uk.hope.osmviewerjetpack.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Area(
    val id: String,
    val type: String,
    // TODO: will need mapping
    val typeId: String,
    val name: String,
) : Parcelable