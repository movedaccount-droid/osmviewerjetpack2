package ac.uk.hope.osmviewerjetpack.musicbrainz

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
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.toNetwork
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.FakeMusicBrainzService
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.model.ReleaseGroupNetworkFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

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

        val networkReleaseGroups = listOf(
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
            service = FakeMusicBrainzService(searchReleaseGroupEntries = networkReleaseGroups),
            rateLimiter = RateLimiter(1000),
            dispatcher = dispatcher
        )
        musicBrainzRepository.updateFollowedCaches()
        advanceUntilIdle()

        assert(fakeReleaseGroupDao.releaseGroups.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `make requests if cache outdated`() = runTest(dispatcher) {
        val follows = listOf(
            FollowedLocalFactory.withCount(UP_TO_DATE_MBID, 1),
            FollowedLocalFactory.withCount(OUTDATED_MBID, 0)
        )

        val artists = listOf(
            ArtistWithRelationsLocal(
                ArtistLocalFactory.withMbidAsName(UP_TO_DATE_MBID),
                follow = follows[0]
            ),
            ArtistWithRelationsLocal(
                ArtistLocalFactory.withMbidAsName(OUTDATED_MBID),
                follow = follows[1]
            )
        )

        val networkReleaseGroups = listOf(
            ReleaseGroupNetworkFactory.fromMbidWithArtists(
                "NEW_RELEASE", listOf(artists[1].toNetwork())
            )
        )

        val fakeReleaseGroupDao = FakeReleaseGroupDao()
        val fakeFollowDao = FakeFollowDao(follows)

        val musicBrainzRepository = MusicBrainzRepositoryImpl(
            artistDao = FakeArtistDao(artists, follows),
            areaDao = FakeAreaDao(),
            releaseGroupDao = fakeReleaseGroupDao,
            releaseDao = FakeReleaseDao(),
            followDao = fakeFollowDao,
            notificationDao = FakeNotificationDao(),
            service = FakeMusicBrainzService(searchReleaseGroupEntries = networkReleaseGroups),
            rateLimiter = RateLimiter(1000),
            dispatcher = dispatcher
        )
        musicBrainzRepository.updateFollowedCaches()
        advanceUntilIdle()


        assert(fakeReleaseGroupDao.releaseGroups.size == 1)
        { "releaseGroups size that should have been 0 was ${fakeReleaseGroupDao.releaseGroups.size}" }
        val found = fakeReleaseGroupDao.releaseGroups[0].mbid
        val needed = networkReleaseGroups[0].id
        assert(found == needed)
        { "releaseGroups did not match" }
        assert(fakeFollowDao.follows.size == 2)
        { "extra follow was gained, now ${follows.size} "}
        assert(fakeFollowDao.follows[0].lastSyncCount == follows[0].lastSyncCount)
        { "up-to-date follow was mistakenly updated" }
        assert(fakeFollowDao.follows[0].lastSyncCount == 1)
        { "outdated follow was not updated" }
    }
}