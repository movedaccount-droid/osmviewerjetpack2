package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.FollowDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.NotificationDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.NotificationLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [AreaLocal::class,
        ArtistLocal::class,
        ReleaseGroupLocal::class,
        ReleaseLocal::class,
        FollowLocal::class,
        NotificationLocal::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MusicBrainzDatabase: RoomDatabase() {
    abstract fun artistDao(): ArtistDao
    abstract fun areaDao(): AreaDao
    abstract fun releaseGroupDao(): ReleaseGroupDao
    abstract fun releaseDao(): ReleaseDao
    abstract fun followedDao(): FollowDao
    abstract fun notificationDao(): NotificationDao
}