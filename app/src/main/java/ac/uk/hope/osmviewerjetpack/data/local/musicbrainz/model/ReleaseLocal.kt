package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Release
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "releases"
)
data class ReleaseLocal(
    @PrimaryKey val mbid: String,
    val releaseGroupMbid: String,
    @Embedded val type: TypeLocal?,
    val name: String,
    val disambiguation: String?,
    val date: String,
    val country: String,
    val barcode: Long?,
    @Embedded val media: MediaLocal,
    @Embedded val packaging: PackagingLocal?,
    @Embedded val status: StatusLocal,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

fun ReleaseLocal.toExternal() = Release(
    type = type?.toExternal(),
    name = name,
    disambiguation = disambiguation,
    date = date,
    country = country,
    barcode = barcode,
    tracks = media.tracks.toExternal(),
    format = media.format.toExternal(),
    packaging = packaging?.toExternal(),
    status = status.toExternal(),
)