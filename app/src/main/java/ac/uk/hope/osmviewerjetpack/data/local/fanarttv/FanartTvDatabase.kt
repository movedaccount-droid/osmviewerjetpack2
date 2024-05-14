package ac.uk.hope.osmviewerjetpack.data.local.fanarttv

import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.AlbumImagesDao
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.ArtistImagesDao
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.AlbumImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [AlbumImagesLocal::class, ArtistImagesLocal::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FanartTvDatabase: RoomDatabase() {
    abstract fun albumImagesDao(): AlbumImagesDao
    abstract fun artistImagesDao(): ArtistImagesDao
}