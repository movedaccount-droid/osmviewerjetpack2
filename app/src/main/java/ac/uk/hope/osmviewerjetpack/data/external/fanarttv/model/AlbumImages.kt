package ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model

import android.net.Uri
import androidx.room.PrimaryKey

data class AlbumImages(
    val covers: List<Uri>?,
    val cds: List<Uri>?
) {
    // TODO: sama, move ideal selection here when we implement it
}