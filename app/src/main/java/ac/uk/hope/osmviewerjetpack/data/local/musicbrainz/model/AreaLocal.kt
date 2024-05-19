package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Area
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.AreaNetwork
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

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

fun AreaLocal.toNetwork() = AreaNetwork(
    id = mbid,
    type = type?.name,
    typeId = type?.mbid,
    name = name,
    sortName = sortName,
    lifeSpan = lifeSpan?.toNetwork()
)

fun List<AreaLocal>.toExternal() = map(AreaLocal::toExternal)