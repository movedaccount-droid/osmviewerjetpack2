package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model

import android.net.Uri

class ArtistImages (
    val backgrounds: List<Uri> = listOf(),
    val banners: List<Uri> = listOf(),
    val hdLogos: List<Uri> = listOf(),
    val logos: List<Uri> = listOf(),
    val thumbnails: List<Uri> = listOf(),
) {
    // offering image types in sets of priority, based on availability
    val thumbnail: Uri
        get() = thumbnails.firstOrNull()
            ?: backgrounds.firstOrNull()
            ?: hdLogos.firstOrNull()
            ?: logos.firstOrNull()
            ?: banners.firstOrNull()
            ?: BACKUP_IMAGE_URI

}

// TODO: replace with a sane, local default
private val BACKUP_IMAGE_URI: Uri = Uri.parse(
    Uri.decode(
        "https://www.svgrepo.com/show/401366/cross-mark-button.svg"
    )
)