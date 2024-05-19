package ac.uk.hope.osmviewerjetpack.data.external.mbv.repository

import androidx.work.Configuration

// holds interfaces for work management.
// not sure if we're meant to hold this in a repo. oh well

interface WorkManagerRepository {

    val workManagerConfiguration: Configuration

    fun startSyncWorker()

//    suspend fun reloadSyncWorker()
//
//    fun stopSyncWorker()

}