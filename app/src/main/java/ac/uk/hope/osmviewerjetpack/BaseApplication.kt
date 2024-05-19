package ac.uk.hope.osmviewerjetpack

import ac.uk.hope.osmviewerjetpack.data.external.mbv.repository.WorkManagerRepository
import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// creating the base lifetime of our hilt dependencies

@HiltAndroidApp
class BaseApplication: Application(), Configuration.Provider {

    @Inject
    lateinit var workManagerRepository: WorkManagerRepository

    override val workManagerConfiguration
        get() = workManagerRepository.workManagerConfiguration

}