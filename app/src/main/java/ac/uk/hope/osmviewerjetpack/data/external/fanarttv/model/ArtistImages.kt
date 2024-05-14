package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model

import ac.uk.hope.osmviewerjetpack.displayables.search.BACKUP_IMAGE_URI
import android.net.Uri


// TODO: do we need Parcelize functionality? this is advertised to allow passing objects between
// fragments, which aren't used in compose. even still this is discussed in compose tutorials
// a lot, despite google advice to avoid passing complex objects this way. maybe outdated?
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