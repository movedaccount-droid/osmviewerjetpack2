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
    val area: AreaLocal?,
    @Relation(
        parentColumn = "beginAreaMbid",
        entityColumn = "mbid"
    )
    val beginArea: AreaLocal?,
)

@Entity(
    tableName = "artists"
)
data class ArtistLocal(
    @PrimaryKey val mbid: String,

    // we could split out types into their own table, since they have an mbid, which would allow
    // more complex queries. but room runs an additional query per related object, and since areas
    // also have types, we would be running six queries to retrieve a single artist. which is bad
    // so we keep it embedded, unless we ever need this
    @Embedded val type: TypeLocal?,

    // lifespan on the other hand has no mbid, so it's always embedded
    @Embedded val lifeSpan: LifeSpanLocal?,

    // foreign key lookups
    val areaMbid: String?,
    val beginAreaMbid: String?,

    val name: String,
    val sortName: String,
    val country: String?,
    val disambiguation: String?,
    val tags: Map<String, Int>,
    val cacheTimestamp: Long = System.currentTimeMillis()
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
    tags = artist.tags
)

fun List<ArtistWithRelationsLocal>.toExternal() = map(ArtistWithRelationsLocal::toExternal)