package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.AreaLocal
import kotlinx.coroutines.flow.Flow

class FakeAreaDao: AreaDao {
    override fun observe(mbid: String, timeout: Long): Flow<AreaLocal?> {
        TODO("Not yet implemented")
    }

    override suspend fun upsert(area: AreaLocal) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertAll(area: List<AreaLocal>) {
        TODO("Not yet implemented")
    }

    override suspend fun prune(timeout: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}