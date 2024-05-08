package ac.uk.hope.osmviewerjetpack.domain.fanarttv.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// TODO: we should absolutely be caching these, because this request also gives us all of the
// album images that we will otherwise be calling one-by-one
@Parcelize
data class ArtistImages(
    val background: List<Uri>?,
    val banner: List<Uri>?,
    val hdLogo: List<Uri>?,
    val logo: List<Uri>?,
    val thumbnail: List<Uri>?,
) : Parcelable