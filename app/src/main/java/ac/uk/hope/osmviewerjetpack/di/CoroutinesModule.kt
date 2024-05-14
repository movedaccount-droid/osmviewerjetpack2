package ac.uk.hope.osmviewerjetpack.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

// injecting dispatchers, so that we can replace them with single-thread blocking dispatchers in
// tests for more predictable behavior (and then test with threading later)

// the annotation is because hilt injects based on type.
// say we wanted to add injectable IO dispatchers later. since we'd now have two injectable
// CoroutineDispatchers, it wouldn't be able to identify between them.
// hence we annotate to clarify which we want injected

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

}