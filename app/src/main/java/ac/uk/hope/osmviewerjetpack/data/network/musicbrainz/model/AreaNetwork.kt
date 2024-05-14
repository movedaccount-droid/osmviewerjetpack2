package ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class AreaNetwork(
    val id: String,
    val type: String,
    @SerializedName("type-id")
    val typeId: String,
    val name: String,
    @SerializedName("sort-name")
    val sortName: String,
    @SerializedName("life-span")
    val lifeSpan: LifeSpanNetwork
)

fun AreaNetwork.toLocal() = ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal(
    id = id,
    type = type,
    typeId = typeId,
    name = name,
    sortName = sortName,
    lifeSpan = lifeSpan.toLocal()
)

fun List<AreaNetwork>.toLocal() = map(AreaNetwork::toLocal)