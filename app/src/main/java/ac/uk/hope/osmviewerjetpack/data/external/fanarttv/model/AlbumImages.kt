package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model

import android.net.Uri

data class AlbumImages(
    val covers: List<Uri>,
    val cds: List<Uri>
) {
    val thumbnail: Uri
        get() = covers.firstOrNull()
            ?: cds.firstOrNull()
            ?: BACKUP_IMAGE_URI
}

// TODO: replace with a sane, local default
private val BACKUP_IMAGE_URI: Uri = Uri.parse(
    Uri.decode(
        "https://www.svgrepo.com/show/401366/cross-mark-button.svg"
    )
)