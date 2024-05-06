package ac.uk.hope.osmviewerjetpack.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// from https://nominatim.openstreetmap.org/ui/search.html
// TODO: fill with all api response values

// store our api data as parcelable, so we can push them with intents.
// this way we can push data to layouts instead of having them request it themselves every time
// TODO: we should actually make sure we need parcelable functionality

// this also fits the definitions of
// https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet#7

// we could add a domain layer, but we want all of this data directly, so there's no real need yet
// TODO: yet... but if these are actually optional, we definitely need a mapping class
// TODO: and also, we don't only have nodes, but the api provides a list of Generic Shit!! so we
// TODO: will probably need to filter things through

@Parcelize
@Serializable
data class Node(
    @SerialName("place_id")
    val placeId: Int,
    @SerialName("osm_id")
    val osmId: Int,
    val name: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("address_type")
    val addressType: String, // should probably be an enum, really
    val type: String,
    val lat: Float,
    val lon: Float
) : Parcelable
