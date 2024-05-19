package ac.uk.hope.osmviewerjetpack

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepositoryImpl
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.FollowDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.NotificationDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeAreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeFollowDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeNotificationDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeReleaseDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowLocal
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.FakeMusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetwork
import ac.uk.hope.osmviewerjetpack.util.toDateString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

// we primarily want to know that the pruning and synching work

const val OUTDATED_MBID = "OUTDATED_MBID"
const val UP_TO_DATE_MBID = "UP_TO_DATE_MBID"

class UpdateArtistCacheTests {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `does not make requests if cache not outdated`() = runTest {

        val dispatcher = UnconfinedTestDispatcher()
        val date = Calendar.getInstance()

        val follows = listOf(
            FollowLocal(
                UP_TO_DATE_MBID,
                date,
                1
            )
        )

        val fakeService = FakeMusicBrainzService(
            searchReleaseGroupEntries = listOf(
                ReleaseGroupNetwork(
                    id = "OLD_RELEASE",
                    title = "OLD RELEASE TITLE",
                    firstReleaseDate = date.toDateString()
                )
            )
        )

        val musicBrainzRepository = MusicBrainzRepositoryImpl(
            artistDao = FakeArtistDao(),
            areaDao = FakeAreaDao(),
            releaseGroupDao = FakeReleaseGroupDao(),
            releaseDao = FakeReleaseDao(),
            followDao = FakeFollowDao(follows),
            notificationDao = FakeNotificationDao(),
            service = fakeService,
            rateLimiter = RateLimiter(1000),
            dispatcher = dispatcher
        )

        musicBrainzRepository.updateFollowedCaches()
    }
}