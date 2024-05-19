package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.FollowDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeFollowDao(
    follows: List<FollowLocal> = listOf()
) : FollowDao {

    private val followsMap = follows.associateBy { it.artistMbid }.toMutableMap()

    override fun observe(mbid: String): Flow<FollowLocal?> = flow {
        emit(followsMap[mbid])
    }

    override suspend fun upsert(followLocal: FollowLocal) {
        followsMap[followLocal.artistMbid] = followLocal
    }

    override suspend fun updateLastSyncCount(mbid: String, lastSyncCount: Int) {
        followsMap[mbid]?.let {
            followsMap[mbid] = FollowLocal(
                it.artistMbid,
                it.started,
                lastSyncCount
            )
        }
    }

    override suspend fun delete(mbid: String) {
        followsMap.remove(mbid)
    }

}