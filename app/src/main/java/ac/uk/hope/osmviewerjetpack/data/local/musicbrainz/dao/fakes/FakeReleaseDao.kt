package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseLocal
import kotlinx.coroutines.flow.Flow

class FakeReleaseDao: ReleaseDao {
    override fun observe(mbid: String, timeout: Long): Flow<ReleaseLocal?> {
        TODO("Not yet implemented")
    }

    override suspend fun upsert(release: ReleaseLocal) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertAll(releases: List<ReleaseLocal>) {
        TODO("Not yet implemented")
    }

    override suspend fun prune(timeout: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}