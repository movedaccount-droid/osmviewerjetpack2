package ac.uk.hope.osmviewerjetpack.di

import ac.uk.hope.osmviewerjetpack.BaseApplication
import ac.uk.hope.osmviewerjetpack.network.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.repository.MusicBrainzRepositoryImpl
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

    // TODO: service requires json head + user agent

    @Provides
    @Singleton
    fun provideMusicBrainzService(): MusicBrainzService {
        return Retrofit.Builder()
            .baseUrl("http://musicbrainz.org/ws/2/")
            .build()
            .create(MusicBrainzService::class.java)
    }

    @Provides
    @Singleton
    fun providerMusicBrainzRepository(
        service: MusicBrainzService
    ): MusicBrainzRepository {
        return MusicBrainzRepositoryImpl(service)
    }

}