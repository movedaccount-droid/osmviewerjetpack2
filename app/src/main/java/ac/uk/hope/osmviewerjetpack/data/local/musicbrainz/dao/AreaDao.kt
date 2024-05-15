package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.currentCacheTimeout
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AreaDao {

    @Query("SELECT * FROM areas WHERE mbid = :mbid AND cacheTimestamp > :timeout")
    fun observe(mbid: String, timeout: Long = currentCacheTimeout): Flow<AreaLocal?>

    @Upsert
    suspend fun upsert(area: AreaLocal)

    @Upsert
    suspend fun upsertAll(area: List<AreaLocal>)

    @Query("DELETE FROM areas WHERE cacheTimestamp < :timeout")
    suspend fun prune(timeout: Long = currentCacheTimeout)

    @Query("DELETE FROM areas")
    suspend fun deleteAll()

}