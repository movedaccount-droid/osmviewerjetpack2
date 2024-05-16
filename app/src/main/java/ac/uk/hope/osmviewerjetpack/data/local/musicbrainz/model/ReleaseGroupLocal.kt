package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Release
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.ReleaseGroup
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

data class ReleaseGroupWithReleaseLocal(
    @Embedded val releaseGroup: ReleaseGroupLocal,
    @Relation(
        parentColumn = "mbid",
        entityColumn = "releaseGroupMbid"
    )
    val release: ReleaseLocal?
)

@Entity(
    tableName = "releaseGroups"
)
data class ReleaseGroupLocal (
    @PrimaryKey val mbid: String,
    val types: List<TypeLocal>,
    val name: String,
    val releaseDate: String,
    val disambiguation: String?,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

fun ReleaseGroupLocal.toExternal() = ReleaseGroup(
    mbid = mbid,
    types = types.toExternal(),
    name = name,
    releaseDate = releaseDate,
    disambiguation = disambiguation
)

fun List<ReleaseGroupLocal>.toExternal() = map(ReleaseGroupLocal::toExternal)