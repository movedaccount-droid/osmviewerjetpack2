package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.DetailedNotification
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Notification
import ac.uk.hope.osmviewerjetpack.data.local.util.getCurrentCalendar
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "notifications"
)
data class NotificationLocal(
    @PrimaryKey val releaseGroupMbid: String,
    val sent: Calendar = getCurrentCalendar()
)

fun NotificationLocal.toExternal() = Notification(sent)

fun List<NotificationLocal>.toExternal() = map(NotificationLocal::toExternal)

// multimap for DetailedNotification
fun Map<NotificationLocal, Map<ReleaseGroupLocal, List<ArtistLocal>>>.toExternal()
: List<DetailedNotification> {
    return this.entries.map notifMap@ { notificationPair ->
        val notification = notificationPair.key
        notificationPair.value.map { releasePair ->
            // release is 1:1 with notif, we only need the first entry
            val releaseGroup = releasePair.key
            val artists = releasePair.value
            return@notifMap DetailedNotification(
                sent = notification.sent,
                releaseGroup = releaseGroup.toExternal(),
                artists = artists.toExternal()
            )
        }
        error("unreachable") // i have called a lot of code terrible in this project;
    }                                 // this is by far the worst
}