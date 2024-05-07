package ac.uk.hope.osmviewerjetpack.domain.fanarttv.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// we don't care about any fatv-domain data

@Parcelize
data class AlbumImages(
    val albumCover: List<Uri>?,
    val cdArt: List<Uri>?,
) : Parcelable