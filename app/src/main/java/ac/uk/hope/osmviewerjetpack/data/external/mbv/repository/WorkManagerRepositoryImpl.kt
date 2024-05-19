package ac.uk.hope.osmviewerjetpack.data.external.mbv.repository

import ac.uk.hope.osmviewerjetpack.di.DefaultDispatcher
import ac.uk.hope.osmviewerjetpack.worker.SyncWorker
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.room.OnConflictStrategy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

const val SYNC_WORKER_UNIQUE_NAME = "sync"

class WorkManagerRepositoryImpl(
    private val workerFactory: HiltWorkerFactory,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
): WorkManagerRepository {

    private val workManager = WorkManager.getInstance(context)

    override val workManagerConfiguration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun startSyncWorker() {
        attemptStartSyncWorker(ExistingPeriodicWorkPolicy.KEEP)
    }

    // out of time, probably can't implement...

//    override fun stopSyncWorker() {
//        workManager.cancelUniqueWork(SYNC_WORKER_UNIQUE_NAME)
//    }
//
//    override suspend fun reloadSyncWorker() {
//        withContext(dispatcher) {
//            val existingSyncWorker = workManager
//                // not update, because we want to change interval
//                .getWorkInfosForUniqueWorkFlow(SYNC_WORKER_UNIQUE_NAME)
//                .first()
//            if (existingSyncWorker.size != 0) {
//                attemptStartSyncWorker(ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE)
//            }
//        }
//    }

    private fun attemptStartSyncWorker(conflictStrategy: ExistingPeriodicWorkPolicy) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresStorageNotLow(true)
            .setRequiresDeviceIdle(true)
            .build()

        // we don't want a retry policy besides "try again in 24 hours" - we are already hammering
        // the api to make this work
        val syncWorker =
            PeriodicWorkRequestBuilder<SyncWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(24, TimeUnit.HOURS) // artists are already synced on follow
                .build()

        workManager.enqueueUniquePeriodicWork(
            SYNC_WORKER_UNIQUE_NAME,
            conflictStrategy,
            syncWorker
        )
    }
}