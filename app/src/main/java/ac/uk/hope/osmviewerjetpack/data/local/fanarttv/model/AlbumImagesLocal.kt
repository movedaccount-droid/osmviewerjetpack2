package ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

// we don't care about any fatv-domain data

@Entity(
    tableName = "albumImages"
)
data class AlbumImagesLocal(
    @PrimaryKey val mbid: String,
    val covers: List<Uri> = listOf(),
    val cds: List<Uri> = listOf(),
    val cacheTimestamp: Long = System.currentTimeMillis()
)