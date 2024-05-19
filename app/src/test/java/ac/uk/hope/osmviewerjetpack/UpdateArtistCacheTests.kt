package ac.uk.hope.osmviewerjetpack

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepositoryImpl
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeAreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeFollowDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeNotificationDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeReleaseDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes.FakeReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocalFactory
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowedLocalFactory
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.FakeMusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetworkFactory
import ac.uk.hope.osmviewerjetpack.util.toDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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

    private lateinit var dispatcher: TestDispatcher

    @Before
    fun setup() {
        dispatcher = StandardTestDispatcher()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `does not make requests if cache not outdated`() = runTest(dispatcher) {
        val follows = listOf(
            FollowedLocalFactory.withCount(UP_TO_DATE_MBID, 1)
        )

        val artists = listOf(
            ArtistWithRelationsLocal(
                ArtistLocalFactory.withMbidAsName(UP_TO_DATE_MBID),
                follow = follows[0]
            )
        )

        val releaseGroups = listOf(
            ReleaseGroupNetworkFactory.fromMbid("OLD_RELEASE")
        )

        val fakeReleaseGroupDao = FakeReleaseGroupDao()

        val musicBrainzRepository = MusicBrainzRepositoryImpl(
            artistDao = FakeArtistDao(artists, follows),
            areaDao = FakeAreaDao(),
            releaseGroupDao = fakeReleaseGroupDao,
            releaseDao = FakeReleaseDao(),
            followDao = FakeFollowDao(follows),
            notificationDao = FakeNotificationDao(),
            service = FakeMusicBrainzService(searchReleaseGroupEntries = releaseGroups),
            rateLimiter = RateLimiter(1000),
            dispatcher = dispatcher
        )
        musicBrainzRepository.updateFollowedCaches()
        advanceUntilIdle()

        assert(fakeReleaseGroupDao.releaseGroups.isEmpty())
    }
}