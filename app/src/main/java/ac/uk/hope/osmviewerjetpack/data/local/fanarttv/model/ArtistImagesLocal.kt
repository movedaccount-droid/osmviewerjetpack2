package ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "artistImages"
)
data class ArtistImagesLocal(
    @PrimaryKey val mbid: String,
    val backgrounds: List<Uri> = listOf(),
    val banners: List<Uri> = listOf(),
    val hdLogos: List<Uri> = listOf(),
    val logos: List<Uri> = listOf(),
    val thumbnails: List<Uri> = listOf(),
    val cacheTimestamp: Long = System.currentTimeMillis()
)

fun ArtistImagesLocal.toExternal() = ArtistImages(
    backgrounds = backgrounds,
    banners = banners,
    hdLogos = hdLogos,
    logos = logos,
    thumbnails = thumbnails
)

fun List<ArtistImagesLocal>.toExternal() = map(ArtistImagesLocal::toExternal)