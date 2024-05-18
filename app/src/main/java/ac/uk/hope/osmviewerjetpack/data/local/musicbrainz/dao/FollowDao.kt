package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowLocal
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowDao {

    @Query("SELECT * FROM follow WHERE artistMbid = :mbid")
    fun observe(mbid: String): Flow<FollowLocal?>

    @Upsert
    suspend fun upsert(followLocal: FollowLocal)

    @Query("UPDATE follow SET lastSyncCount = :lastSyncCount WHERE artistMbid = :mbid")
    suspend fun updateLastSyncCount(mbid: String, lastSyncCount: Int)

    @Query("DELETE FROM follow WHERE artistMbid = :mbid")
    suspend fun delete(mbid: String)

}