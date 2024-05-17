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

    @Query("SELECT * FROM artists " +
            "INNER JOIN followed ON followed.artistMbid = artists.mbid " +
            "WHERE followed.artistMbid IS NOT NULL " +
            "AND cacheTimestamp > :timeout")
    fun observeFollowed(timeout: Long = currentCacheTimeout): Flow<List<ArtistWithRelationsLocal>>

    @Upsert
    suspend fun upsert(artist: ArtistLocal)

    @Upsert
    suspend fun upsertAll(artist: List<ArtistLocal>)

    @Query("DELETE FROM artists " +
            "WHERE ROWID IN (" +
                "SELECT a.ROWID FROM artists a " +
            "INNER JOIN followed f ON f.artistMbid = a.mbid " +
                "WHERE f.artistMbid IS NULL AND a.cacheTimestamp < :timeout" +
            ")")
    suspend fun prune(timeout: Long = currentCacheTimeout)

    @Query("DELETE FROM artists")
    suspend fun deleteAll()

}