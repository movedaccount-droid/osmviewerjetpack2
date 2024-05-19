package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeReleaseGroupDao(
    releaseGroups: List<ReleaseGroupLocal> = listOf(),
    releases: List<ReleaseLocal> = listOf(),
): ReleaseGroupDao {

    private val releaseGroupsMap = releaseGroups.associateBy { it.mbid }.toMutableMap()
    val releaseGroups
        get() = releaseGroupsMap.values.toList()
    private val releasesMap = releases.associateBy { it.releaseGroupMbid }.toMutableMap()

    override fun observe(mbid: String, timeout: Long): Flow<ReleaseGroupLocal?> = flow {
            var observed = releaseGroupsMap[mbid]
            observed?.let { if (it.cacheTimestamp > timeout) observed = null }
            emit(observed)
        }

    override fun observeAll(mbids: List<String>, timeout: Long): Flow<List<ReleaseGroupLocal>> =
        flow {
            emit(
                releaseGroupsMap
                    .filterKeys { mbids.contains(it) }
                    .filterValues { it.cacheTimestamp > timeout }
                    .values.toList()
            )
        }

    override fun observeWithRelationships(
        mbid: String,
        timeout: Long
    ): Flow<Map<ReleaseGroupLocal, ReleaseLocal?>> {
        TODO("Not yet implemented")
    }

    override suspend fun upsert(releaseGroup: ReleaseGroupLocal) {
        releaseGroupsMap[releaseGroup.mbid] = releaseGroup
    }

    override suspend fun upsertAll(releaseGroups: List<ReleaseGroupLocal>) {
        TODO("Not yet implemented")
    }

    override suspend fun prune(timeout: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}