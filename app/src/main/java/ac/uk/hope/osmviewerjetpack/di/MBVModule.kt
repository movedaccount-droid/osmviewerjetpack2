package ac.uk.hope.osmviewerjetpack.di

import ac.uk.hope.osmviewerjetpack.data.external.mbv.repository.WorkManagerRepository
import ac.uk.hope.osmviewerjetpack.data.external.mbv.repository.WorkManagerRepositoryImpl
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

// anything local data related

@Module
@InstallIn(SingletonComponent::class)
object MBVModule {

    @Provides
    fun provideWorkManagerRepository(
        workerFactory: HiltWorkerFactory,
        @ApplicationContext context: Context,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): WorkManagerRepository = WorkManagerRepositoryImpl(
        workerFactory, context, dispatcher
    )

}