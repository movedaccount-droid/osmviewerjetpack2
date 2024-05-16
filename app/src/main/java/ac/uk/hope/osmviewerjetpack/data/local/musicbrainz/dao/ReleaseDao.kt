package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.currentCacheTimeout
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ReleaseDao {

    @Query("SELECT * FROM releases WHERE mbid = :mbid AND cacheTimestamp > :timeout")
    fun observe(mbid: String, timeout: Long = currentCacheTimeout): Flow<ReleaseLocal?>

    @Upsert
    suspend fun upsert(release: ReleaseLocal)

    @Upsert
    suspend fun upsertAll(releases: List<ReleaseLocal>)

    @Query("DELETE FROM releases WHERE cacheTimestamp < :timeout")
    suspend fun prune(timeout: Long = currentCacheTimeout)

    @Query("DELETE FROM releases")
    suspend fun deleteAll()
    
}