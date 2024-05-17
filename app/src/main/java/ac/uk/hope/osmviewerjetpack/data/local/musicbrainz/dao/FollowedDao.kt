package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowedLocal
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowedDao {

    @Query("SELECT * FROM followed WHERE artistMbid = :mbid")
    fun getFollowed(mbid: String): Flow<FollowedLocal?>

}