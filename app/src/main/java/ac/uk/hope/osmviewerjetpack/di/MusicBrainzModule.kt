package ac.uk.hope.osmviewerjetpack.di

import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.data.external.musicbrainz.repository.MusicBrainzRepositoryImpl
import ac.uk.hope.osmviewerjetpack.data.external.util.RateLimiter
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.MusicBrainzDatabase
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.AreaDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ArtistDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.FollowedDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseDao
import ac.uk.hope.osmviewerjetpack.data.local.musicbrainz.dao.ReleaseGroupDao
import ac.uk.hope.osmviewerjetpack.data.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.di.util.getBaseHttpClientBuilder
import ac.uk.hope.osmviewerjetpack.di.util.getBaseRetrofitBuilder
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class MusicBrainzLimiter

@Module
@InstallIn(SingletonComponent::class)
class MusicBrainzModule {

    @Provides
    @Singleton
    fun provideMusicBrainzService(): MusicBrainzService {
        val httpBuilder = getBaseHttpClientBuilder(mapOf("Accept" to "application/json"))
        return getBaseRetrofitBuilder("https://musicbrainz.org/ws/2/", httpBuilder)
            .build()
            .create(MusicBrainzService::class.java)

    }

    @Provides
    @Singleton
    fun provideMusicBrainzDatabase(@ApplicationContext context: Context): MusicBrainzDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MusicBrainzDatabase::class.java,
            "MusicBrainz.db"
        ).build()
    }

    @Provides
    fun provideArtistDao(database: MusicBrainzDatabase): ArtistDao
            = database.artistDao()

    @Provides
    fun provideAreaDao(database: MusicBrainzDatabase): AreaDao
            = database.areaDao()

    @Provides
    fun provideReleaseGroupDao(database: MusicBrainzDatabase): ReleaseGroupDao
            = database.releaseGroupDao()

    @Provides
    fun provideReleaseDao(database: MusicBrainzDatabase): ReleaseDao
            = database.releaseDao()

    @Provides
    fun provideFollowedDao(database: MusicBrainzDatabase): FollowedDao
            = database.followedDao()

    @Provides
    @Singleton
    @MusicBrainzLimiter
    fun provideMusicBrainzLimiter(): RateLimiter
            = RateLimiter(1000)

    @Provides
    @Singleton
    fun provideMusicBrainzRepository(
        artistDao: ArtistDao,
        areaDao: AreaDao,
        releaseGroupDao: ReleaseGroupDao,
        releaseDao: ReleaseDao,
        followedDao: FollowedDao,
        service: MusicBrainzService,
        @MusicBrainzLimiter rateLimiter: RateLimiter,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): MusicBrainzRepository {
        return MusicBrainzRepositoryImpl(
            artistDao, areaDao, releaseGroupDao, releaseDao, followedDao,
            service, rateLimiter, dispatcher)
    }

}