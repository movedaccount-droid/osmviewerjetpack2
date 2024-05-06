package ac.uk.hope.osmviewerjetpack.di

import ac.uk.hope.osmviewerjetpack.BaseApplication
import ac.uk.hope.osmviewerjetpack.data.repository.NominatimRepository
import ac.uk.hope.osmviewerjetpack.data.repository.NominatimRepositoryImpl
import ac.uk.hope.osmviewerjetpack.data.service.service.NominatimService
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

// providing services and repositories with application lifetime

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // TODO: everyone seems to talk about this and i have no idea when we might need it
    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context): BaseApplication{
        return app as BaseApplication
    }

    @Provides
    @Singleton
    fun provideNominatimService(): NominatimService {
        return Retrofit.Builder()
            .baseUrl("some")
            .build()
            .create(NominatimService::class.java)
    }

    @Provides
    @Singleton
    fun providerNominatimRepository(
        service: NominatimService
    ): NominatimRepository {
        return NominatimRepositoryImpl(service)
    }

}