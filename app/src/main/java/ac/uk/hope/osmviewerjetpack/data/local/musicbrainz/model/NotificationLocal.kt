package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

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