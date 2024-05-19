package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Artist
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


data class ArtistWithRelationsLocal(
    @Embedded val artist: ArtistLocal,
    @Relation(
        parentColumn = "areaMbid",
        entityColumn = "mbid"
    )
    val area: AreaLocal? = null,
    @Relation(
        parentColumn = "beginAreaMbid",
        entityColumn = "mbid"
    )
    val beginArea: AreaLocal? = null,
    @Relation(
        parentColumn = "mbid",
        entityColumn = "artistMbid"
    )
    val follow: FollowLocal? = null
)

@Entity(
    tableName = "artists"
)
data class ArtistLocal(
    @PrimaryKey val mbid: String,
    val name: String,
    val sortName: String,

    // foreign key lookups
    val areaMbid: String? = null,
    val beginAreaMbid: String? = null,

    // we could split out types into their own table, since they have an mbid, which would allow
    // more complex queries. but room runs an additional query per related object, and since areas
    // also have types, we would be running six queries to retrieve a single artist. which is bad
    // so we keep it embedded, unless we ever need this
    @Embedded val type: TypeLocal? = null,

    val country: String? = null,
    val disambiguation: String? = null,
    @Embedded val lifeSpan: LifeSpanLocal? = null,
    val tags: Map<String, Int> = mapOf(),
    val cacheTimestamp: Long = System.currentTimeMillis()
)

fun ArtistLocal.toExternal() = Artist(
    mbid = mbid,
    type = type?.toExternal(),
    name = name,
    sortName = sortName,
    country = country,
    disambiguation = disambiguation,
    lifeSpan = lifeSpan?.toExternal(),
    tags = tags,
)

fun ArtistWithRelationsLocal.toExternal() = Artist(
    mbid = artist.mbid,
    type = artist.type?.toExternal(),
    name = artist.name,
    sortName = artist.sortName,
    country = artist.country,
    area = area?.toExternal(),
    beginArea = beginArea?.toExternal(),
    disambiguation = artist.disambiguation,
    lifeSpan = artist.lifeSpan?.toExternal(),
    tags = artist.tags,
    followed = follow != null
)

@JvmName("toExternalArtist")
fun List<ArtistLocal>.toExternal() = map(ArtistLocal::toExternal)

@JvmName("toExternalRelatedArtist")
fun List<ArtistWithRelationsLocal>.toExternal() = map(ArtistWithRelationsLocal::toExternal)

object ArtistLocalFactory {
    fun withMbidAsName(mbid: String) = ArtistLocal(
        mbid, mbid, mbid
    )
}