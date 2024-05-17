package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowedLocal
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowedDao {

    @Query("SELECT * FROM followed WHERE artistMbid = :mbid")
    fun getFollowed(mbid: String): Flow<FollowedLocal?>

    @Upsert
    suspend fun addFollowed(followedLocal: FollowedLocal)

    @Query("DELETE FROM followed WHERE artistMbid = :mbid")
    suspend fun removeFollowed(mbid: String)

}