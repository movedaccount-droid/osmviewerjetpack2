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

    @Query(OBSERVE_QUERY)
    fun observe(mbid: String, timeout: Long = currentCacheTimeout): Flow<ArtistWithRelationsLocal?>

    @Query(OBSERVE_ALL_QUERY)
    fun observeAll(
        mbids: List<String>,
        timeout: Long = currentCacheTimeout
    ): Flow<List<ArtistWithRelationsLocal?>>

    @Query(OBSERVE_FOLLOWED_QUERY)
    fun observeFollowed(timeout: Long = currentCacheTimeout): Flow<List<ArtistWithRelationsLocal>>

    @Upsert
    suspend fun upsert(artist: ArtistLocal)

    @Upsert
    suspend fun upsertAll(artist: List<ArtistLocal>)

    @Query(PRUNE_QUERY)
    suspend fun prune(timeout: Long = currentCacheTimeout)

    @Query("DELETE FROM artists")
    suspend fun deleteAll()

}

// TODO: we should use a view for valid cache selection
private const val OBSERVE_QUERY = """
    SELECT * FROM artists
    WHERE mbid = :mbid
    AND cacheTimestamp > :timeout
"""

private const val OBSERVE_ALL_QUERY = """
    SELECT * FROM artists
    WHERE mbid IN (:mbids)
    AND cacheTimestamp > :timeout
"""

private const val OBSERVE_FOLLOWED_QUERY = """
    SELECT * FROM artists
    INNER JOIN follow ON follow.artistMbid = artists.mbid
    WHERE follow.artistMbid IS NOT NULL
    AND cacheTimestamp > :timeout
"""

private const val PRUNE_QUERY = """
    DELETE FROM artists
    WHERE ROWID IN (
        SELECT a.ROWID FROM artists a
        INNER JOIN follow f ON f.artistMbid = a.mbid
        WHERE f.artistMbid IS NULL AND a.cacheTimestamp < :timeout
    )
"""