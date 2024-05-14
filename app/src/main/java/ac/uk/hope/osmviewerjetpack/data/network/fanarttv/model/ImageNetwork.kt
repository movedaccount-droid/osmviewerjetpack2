package ac.uk.hope.osmviewerjetpack.data.network.fanarttv.model

import android.net.Uri
import kotlinx.serialization.Serializable


@Serializable
data class ImageNetwork(
    val id: Int,
    val url: String,
    val likes: Int,
    val disc: Int,
    val size: Int,
)

// TODO: similarly we should probably not be abstracting here
// this should be nullable, but if the api starts returning invalid uris
// a crash is preferable so we can learn when/why
fun ImageNetwork.toLocal(): Uri = Uri.parse(Uri.decode(url))

fun List<ImageNetwork>.toLocal() = map(ImageNetwork::toLocal)