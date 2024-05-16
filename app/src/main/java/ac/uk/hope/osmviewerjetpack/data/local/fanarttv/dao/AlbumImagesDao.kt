package ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.model.AlbumImages
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.AlbumImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.util.CACHE_TIMEOUT
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumImagesDao {

    @Query("SELECT * FROM albumImages WHERE mbid = :mbid")
    fun observe(mbid: String): Flow<AlbumImages?>

    @Upsert
    suspend fun upsert(albumImages: AlbumImagesLocal)

    @Upsert
    suspend fun upsertAll(albumImages: List<AlbumImagesLocal>)

    @Query("DELETE FROM albumImages WHERE cacheTimestamp < :timeout")
    suspend fun prune(timeout: Long = System.currentTimeMillis() - CACHE_TIMEOUT)

    @Query("DELETE FROM albumImages")
    suspend fun deleteAll()

}