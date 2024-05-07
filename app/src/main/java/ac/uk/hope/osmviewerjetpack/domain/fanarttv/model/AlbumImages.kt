package ac.uk.hope.osmviewerjetpack.domain.fanarttv.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.net.URL

// we don't care about any fatv-domain data

@Parcelize
data class AlbumImages(
    val albumCover: List<URL>?,
    val cdArt: List<URL>?,
) : Parcelable