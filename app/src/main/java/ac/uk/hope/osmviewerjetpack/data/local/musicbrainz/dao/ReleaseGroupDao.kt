package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupWithReleaseLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.currentCacheTimeout
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ReleaseGroupDao {

    @Query("SELECT * FROM releaseGroups WHERE mbid = :mbid AND cacheTimestamp > :timeout")
    fun observe(mbid: String, timeout: Long = currentCacheTimeout): Flow<ReleaseGroupLocal?>

    @Query("SELECT * FROM releaseGroups WHERE mbid = :mbid AND cacheTimestamp > :timeout")
    fun observeWithRelationships(
        mbid: String,
        timeout: Long = currentCacheTimeout
    ): Flow<ReleaseGroupWithReleaseLocal?>

    @Upsert
    suspend fun upsert(releaseGroup: ReleaseGroupLocal)

    @Upsert
    suspend fun upsertAll(releaseGroups: List<ReleaseGroupLocal>)

    @Query("DELETE FROM releaseGroups WHERE cacheTimestamp < :timeout")
    suspend fun prune(timeout: Long = currentCacheTimeout)

    @Query("DELETE FROM releaseGroups")
    suspend fun deleteAll()

}