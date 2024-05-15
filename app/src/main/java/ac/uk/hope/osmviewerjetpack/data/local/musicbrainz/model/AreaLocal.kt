package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Area
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "areas"
)
data class AreaLocal(
    @PrimaryKey val mbid: String,
    @Embedded val type: TypeLocal?,
    val name: String,
    val sortName: String,
    @Embedded val lifeSpan: LifeSpanLocal?,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

fun AreaLocal.toExternal() = Area(
    type = type?.toExternal(),
    name = name,
    sortName = sortName,
    lifeSpan = lifeSpan?.toExternal()
)

fun List<AreaLocal>.toExternal() = map(AreaLocal::toExternal)