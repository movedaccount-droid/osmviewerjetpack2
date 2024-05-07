package ac.uk.hope.osmviewerjetpack.domain.fanarttv.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.net.URL

// TODO: we should absolutely be caching these, because this request also gives us all of the
// album images that we will otherwise be calling one-by-one
@Parcelize
data class ArtistImages(
    val background: List<URL>,
    val banner: List<URL>,
    val hdLogo: List<URL>,
    val logo: List<URL>,
    val thumbnail: List<URL>,
) : Parcelable