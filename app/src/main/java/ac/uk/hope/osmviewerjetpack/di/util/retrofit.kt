package ac.uk.hope.osmviewerjetpack.di.util

import ac.uk.hope.osmviewerjetpack.util.HTTP_LOGGING_LEVEL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getBaseHttpClientBuilder(headers: Map<String, String>): OkHttpClient.Builder {
    val httpClient = OkHttpClient.Builder()

    // enabling logging
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HTTP_LOGGING_LEVEL)
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

fun getBaseRetrofitBuilder(
    url: String,
    httpBuilder: OkHttpClient.Builder
): Retrofit.Builder {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpBuilder.build())
        .baseUrl(url)
}