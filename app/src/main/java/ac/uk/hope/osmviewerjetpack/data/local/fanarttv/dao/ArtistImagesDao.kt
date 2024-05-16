package ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.ArtistImages
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.currentCacheTimeout
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistImagesDao {

    @Query("SELECT * FROM artistImages WHERE mbid = :mbid AND cacheTimestamp > :timeout")
    fun observe(mbid: String, timeout: Long = currentCacheTimeout): Flow<ArtistImages?>

    @Upsert
    suspend fun upsert(artistImages: ArtistImagesLocal)

    @Upsert
    suspend fun upsertAll(artistImages: List<ArtistImagesLocal>)

    // TODO: we should regularly run this in a service, so that the cache does not become
    // too large with dead information
    @Query("DELETE FROM artistImages WHERE cacheTimestamp < :timeout")
    suspend fun prune(timeout: Long = currentCacheTimeout)

    @Query("DELETE FROM artistImages")
    suspend fun deleteAll()

}