package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

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
    val release: ReleaseLocal?,
)

@Entity(
    tableName = "releaseGroups"
)
data class ReleaseGroupLocal (
    @PrimaryKey val mbid: String,

    // this should be a relationship, but we hit problems. if we make an M:N relationship
    // and look it up, we will only look up into our cache. in that case, how can we get
    // the related artists from the network as a backup? the only option is to manually look up
    // the crossref objects, then retrieve those artists by hand, then construct our final object,
    // running a huge number of queries and delay just to get the final object.
    // this is silly and we only need these to check subscriptions anyway. so whatever
    val artistMbids: List<String>,

    val types: List<TypeLocal>,
    val name: String,
    val releaseDate: String,
    val disambiguation: String?,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

fun ReleaseGroupLocal.toExternal() = ReleaseGroup(
    mbid = mbid,
    artistMbids = artistMbids,
    types = types.toExternal(),
    name = name,
    releaseDate = releaseDate,
    disambiguation = disambiguation
)

fun List<ReleaseGroupLocal>.toExternal() = map(ReleaseGroupLocal::toExternal)