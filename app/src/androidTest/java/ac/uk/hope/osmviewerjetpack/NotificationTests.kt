package ac.uk.hope.osmviewerjetpack

import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.FanartTvDatabase
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.ArtistImagesDao
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.ArtistImagesLocal
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.model.toExternal
import ac.uk.hope.osmviewerjetpack.data.local.util.currentCacheTimeout
import ac.uk.hope.osmviewerjetpack.worker.SyncWorker
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.testing.TestListenableWorkerBuilder
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NotificationTests {
    private lateinit var artistImagesDao: ArtistImagesDao
    private lateinit var fanartTvDatabase: FanartTvDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        fanartTvDatabase = Room.inMemoryDatabaseBuilder(
            context,
            FanartTvDatabase::class.java
        ).build()
        artistImagesDao = fanartTvDatabase.artistImagesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        fanartTvDatabase.close()
    }

    @Test
    @Throws(IOException::class)
    fun pruneClearsOldData() = runTest {
        val artistImages =
            ArtistImagesLocal(
                "OLD_DATA_MBID",
                cacheTimestamp = 100
            )

        artistImagesDao.upsert(artistImages)
        artistImagesDao.prune()

        val found = artistImagesDao.observe("OLD_DATA_MBID").first()
        assertEquals(found, null)
    }

    @Test
    @Throws(IOException::class)
    fun pruneDoesNotClearNewData() = runTest {
        val artistImages =
            ArtistImagesLocal(
                "OLD_DATA_MBID",
                cacheTimestamp = currentCacheTimeout + 100000
            )

        artistImagesDao.upsert(artistImages)
        artistImagesDao.prune()

        val found = artistImagesDao.observe("OLD_DATA_MBID").first()
        assert(found != null)
        { "object was pruned" }
    }
}