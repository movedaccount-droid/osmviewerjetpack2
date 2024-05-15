package ac.uk.hope.osmviewerjetpack.di

import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.data.external.fanarttv.repository.FanartTvRepositoryImpl
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.FanartTvDatabase
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.AlbumImagesDao
import ac.uk.hope.osmviewerjetpack.data.local.fanarttv.dao.ArtistImagesDao
import ac.uk.hope.osmviewerjetpack.data.network.fanarttv.FanartTvService
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FanartTvModule {

    @Provides
    @Singleton
    fun provideFanartTvService(): FanartTvService {

        // TODO: there's probably a central/secure way for storing keys rather than this
        val httpBuilder = getBaseHttpClientBuilder(
            mapOf(
                "api-key" to "0e6e5e68a5f5fd468a7d93a9a63cfee3"
            )
        )
        return getBaseRetrofitBuilder("https://webservice.fanart.tv/v3/", httpBuilder)
            .build()
            .create(FanartTvService::class.java)

    }

    @Provides
    @Singleton
    fun provideFanartTvDatabase(@ApplicationContext context: Context): FanartTvDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FanartTvDatabase::class.java,
            "FanartTV.db"
        ).build()
    }

    @Provides
    fun provideAlbumImagesDao(database: FanartTvDatabase): AlbumImagesDao
            = database.albumImagesDao()

    @Provides
    fun provideArtistImagesDao(database: FanartTvDatabase): ArtistImagesDao
            = database.artistImagesDao()

    @Provides
    @Singleton
    fun provideFanartTvRepository(
        albumImagesDao: AlbumImagesDao,
        artistImagesDao: ArtistImagesDao,
        service: FanartTvService,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): FanartTvRepository = FanartTvRepositoryImpl(
        albumImagesDao,
        artistImagesDao,
        service,
        dispatcher
    )

}