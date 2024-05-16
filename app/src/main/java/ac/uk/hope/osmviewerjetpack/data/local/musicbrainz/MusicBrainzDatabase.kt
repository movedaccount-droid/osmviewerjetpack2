package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [AreaLocal::class, ArtistLocal::class, ReleaseGroupLocal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MusicBrainzDatabase: RoomDatabase() {
    abstract fun artistDao(): ArtistDao
    abstract fun areaDao(): AreaDao

    abstract fun releaseGroupDao(): ReleaseGroupDao
}