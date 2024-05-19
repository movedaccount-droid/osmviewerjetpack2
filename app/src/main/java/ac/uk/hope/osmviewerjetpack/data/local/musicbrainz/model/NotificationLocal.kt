package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.DetailedNotification
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.model.Notification
import ac.uk.hope.osmviewerjetpack.util.getCurrentCalendar
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
fun Map<NotificationLocal, ReleaseGroupLocal>.toExternal()
: List<DetailedNotification> {
    return this.entries.map {
        DetailedNotification(
            sent = it.key.sent,
            releaseGroup = it.value.toExternal()
        )
    }
}