package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.NotificationLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query(OBSERVE_ALL_QUERY)
    fun observeAll(): Flow<List<NotificationLocal>>

    @Transaction
    @Query(OBSERVE_ALL_DETAILED_QUERY)
    fun observeAllWithDetailedReleaseGroups(): Flow<Map<NotificationLocal, ReleaseGroupLocal>>

    @Upsert
    suspend fun upsert(notification: NotificationLocal)

    @Query(DELETE_QUERY)
    suspend fun delete(releaseGroupMbid: String)

}

private const val OBSERVE_ALL_QUERY = """
SELECT * FROM notifications
"""

private const val OBSERVE_ALL_DETAILED_QUERY = """
SELECT * FROM notifications n
INNER JOIN releaseGroups rg
ON rg.mbid = n.releaseGroupMbid
"""

private const val DELETE_QUERY = """
DELETE FROM notifications
WHERE releaseGroupMbid = :releaseGroupMbid
"""