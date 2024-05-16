package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.currentCacheTimeout
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artists WHERE mbid = :mbid AND cacheTimestamp > :timeout")
    fun observe(mbid: String, timeout: Long = currentCacheTimeout): Flow<ArtistWithRelationsLocal?>

    @Upsert
    suspend fun upsert(artist: ArtistLocal)

    @Upsert
    suspend fun upsertAll(artist: List<ArtistLocal>)

    @Query("DELETE FROM artists WHERE cacheTimestamp < :timeout AND NOT followed")
    suspend fun prune(timeout: Long = currentCacheTimeout)

    @Query("DELETE FROM artists")
    suspend fun deleteAll()

}