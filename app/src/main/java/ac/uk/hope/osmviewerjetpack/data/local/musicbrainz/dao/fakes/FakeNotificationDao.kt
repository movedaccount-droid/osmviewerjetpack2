package ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.fakes

import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.NotificationDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.NotificationLocal
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.model.ReleaseGroupLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNotificationDao(
    notifications: List<NotificationLocal> = listOf()
): NotificationDao {

    private val notificationsMap = notifications.associateBy { it.releaseGroupMbid }.toMutableMap()

    override fun observe(releaseGroupMbid: String): Flow<NotificationLocal?> = flow {
        emit (
            notificationsMap[releaseGroupMbid]
        )
    }

    override fun observeAll(): Flow<List<NotificationLocal>> = flow {
        emit (
            notificationsMap.values.toList()
        )
    }

    override fun observeAllWithDetailedReleaseGroups(): Flow<Map<NotificationLocal, ReleaseGroupLocal>> {
        TODO("Not yet implemented")
    }

    override suspend fun upsert(notification: NotificationLocal) {
        notificationsMap[notification.releaseGroupMbid] = notification
    }

    override suspend fun delete(releaseGroupMbid: String) {
        TODO("Not yet implemented")
    }
}