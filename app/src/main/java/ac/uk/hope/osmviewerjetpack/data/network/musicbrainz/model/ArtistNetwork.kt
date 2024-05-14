package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistNetwork(
    val id: String,
    val type: String?,
    @SerializedName("type-id")
    val typeId: String?,
    val score: Int,
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

fun ArtistNetwork.toLocal() = ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal(
    id = id,
    type = type,
    typeId = typeId,
    score = score,
    name = name,
    sortName = sortName,
    country = country,
    area = area?.toLocal(),
    beginArea = beginArea?.toLocal(),
    disambiguation = disambiguation,
    lifeSpan = lifeSpan.toLocal(),
    tags = tags?.toLocal()
)

fun List<ArtistNetwork>.toLocal() = map(ArtistNetwork::toLocal)