package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ArtistWithRelationsLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.FollowLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeArtistDao(
    artists: List<ArtistWithRelationsLocal> = listOf(),
    follows: List<FollowLocal> = listOf()
): ArtistDao {

    private val artistsMap = artists.associateBy { it.artist.mbid }.toMutableMap()
    private val followsMap = follows.associateBy { it.artistMbid }.toMutableMap()

    override fun observe(mbid: String, timeout: Long): Flow<ArtistWithRelationsLocal?> = flow {
        var observed = artistsMap[mbid]
        observed?.let { if (it.artist.cacheTimestamp > timeout) observed = null }
        emit(observed)
    }

    override fun observeAll(
        mbids: List<String>,
        timeout: Long
    ): Flow<List<ArtistWithRelationsLocal?>> {
        TODO("Not yet implemented")
    }

    override fun observeFollowed(timeout: Long): Flow<List<ArtistWithRelationsLocal>> = flow {
        emit(
            artistsMap
                .filterKeys { followsMap.keys.contains(it) }
                .filterValues { it.artist.cacheTimestamp > timeout }
                .values.toList()
        )
    }

    override suspend fun upsert(artist: ArtistLocal) {
        TODO("Not yet implemented")
    }

    override suspend fun upsertAll(artist: List<ArtistLocal>) {
        TODO("Not yet implemented")
    }

    override suspend fun prune(timeout: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}