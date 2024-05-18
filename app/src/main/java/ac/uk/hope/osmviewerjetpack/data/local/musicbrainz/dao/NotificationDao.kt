package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.NotificationLocal
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * from notifications")
    fun observeAll(): Flow<List<NotificationLocal>>

    @Upsert
    suspend fun upsert(notification: NotificationLocal)

    @Query("DELETE FROM notifications " +
            "WHERE releaseGroupMbid = :releaseGroupMbid")
    suspend fun delete(releaseGroupMbid: String)

}