package ac.uk.hope.osmviewerjetpack.di

import ac.uk.hope.osmviewerjetpack.BaseApplication
import ac.uk.hope.osmviewerjetpack.network.fanarttv.FanartTvService
import ac.uk.hope.osmviewerjetpack.network.musicbrainz.MusicBrainzService
import ac.uk.hope.osmviewerjetpack.repository.fanarttv.FanartTvRepository
import ac.uk.hope.osmviewerjetpack.repository.fanarttv.FanartTvRepositoryImpl
import ac.uk.hope.osmviewerjetpack.repository.musicbrainz.MusicBrainzRepository
import ac.uk.hope.osmviewerjetpack.repository.musicbrainz.MusicBrainzRepositoryImpl
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// providing services and repositories with application lifetime

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // TODO: everyone seems to talk about this and i have no idea when we might need it
    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

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
    fun provideMusicBrainzRepository(
        service: MusicBrainzService
    ): MusicBrainzRepository {
        return MusicBrainzRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideFanartTvService(): FanartTvService {

        // TODO: get this key
        // TODO: there's probably a central/secure way for storing keys rather than this
        val httpBuilder = getBaseHttpClientBuilder(mapOf(
            "api-key" to "0e6e5e68a5f5fd468a7d93a9a63cfee3")
        )
        return getBaseRetrofitBuilder("http://webservice.fanart.tv/v3/", httpBuilder)
            .build()
            .create(FanartTvService::class.java)

    }

    @Provides
    @Singleton
    fun provideFanartTvRepository(
        service: FanartTvService
    ): FanartTvRepository {
        return FanartTvRepositoryImpl(service)
    }

}

private fun getBaseHttpClientBuilder(headers: Map<String, String>): OkHttpClient.Builder {
    val httpClient = OkHttpClient.Builder()

    // enabling logging
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    httpClient.addInterceptor(logging)

    // replacing headers
    // https://stackoverflow.com/questions/32605711/adding-header-to-all-request-with-retrofit-2
    httpClient.addInterceptor{
        val requestBuilder = it.request()
            .newBuilder()
            .addHeader(
                "User-Agent",
                "MusicBrainzViewer/0.0.1 ( 20202025@hope.ac.uk )"
            )
        headers.forEach { entry ->
            requestBuilder.addHeader(entry.key, entry.value)
        }
        it.proceed(
            requestBuilder.build()
        )
    }

    return httpClient
}

private fun getBaseRetrofitBuilder(
    url: String,
    httpBuilder: OkHttpClient.Builder
): Retrofit.Builder {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpBuilder.build())
        .baseUrl(url)
}