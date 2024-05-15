package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.TypeLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistNetwork(
    val id: String,
    val type: String?,
    @SerializedName("type-id")
    val typeId: String?,
    val name: String,
    @SerializedName("sort-name")
    val sortName: String,
    val country: String?,
    val area: AreaNetwork?,
    @SerializedName("begin-area")
    val beginArea: AreaNetwork?,
    val disambiguation: String?,
    @SerializedName("life-span")
    val lifeSpan: LifeSpanNetwork,
    val tags: List<TagNetwork>?,
)

fun ArtistNetwork.toLocal() = ArtistWithRelationsLocal(
    artist = ArtistLocal(
        mbid = id,
        type = typeId?.let { TypeLocal(mbid = it, name = type!!) },
        name = name,
        sortName = sortName,
        country = country,
        disambiguation = disambiguation,
        lifeSpan = lifeSpan.toLocal(),
        tags = tags?.toLocal(),
        areaMbid = area?.id,
        beginAreaMbid = beginArea?.id
    ),
    area = area?.toLocal(),
    beginArea = beginArea?.toLocal(),
)

fun List<ArtistNetwork>.toLocal() = map(ArtistNetwork::toLocal)