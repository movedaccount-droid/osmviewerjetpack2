package ac.uk.hope.osmviewerjetpack.di

import ac.uk.hope.osmviewerjetpack.data.repository.NominatimRepository
import ac.uk.hope.osmviewerjetpack.data.repository.NominatimRepositoryImpl
import ac.uk.hope.osmviewerjetpack.data.service.service.NominatimService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

// providing services and repositories with application lifetime

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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