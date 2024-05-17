package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

// there is no simple way to have a field in an object, but then not update it when we
// retrieve it from the network, only provide it on initialization. this would require an Update
// object and to check manually if the entity is in the table, requiring an extra query per artist,
// and bypassing the @Upsert function

// so instead we have to have an association which is literally just "yes, we are followed"
// this is about the point where my commits devolve into new and fascinating swears

@Entity(
    tableName = "followed"
)
data class FollowedLocal(
    @PrimaryKey val artistMbid: String,
    val started: Calendar,
    val newReleases: List<String>
)

object FollowedLocalFactory {
    fun create(mbid: String): FollowedLocal = FollowedLocal(mbid, getCurrentDate(), listOf())
}

// can't use new date apis, too new
private fun getCurrentDate(): Calendar {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal
}