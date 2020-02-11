package org.studio.network.koin

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.studio.network.BuildConfig.STUDIO_GHIBLI_BASE_URL
import org.studio.network.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val OK_HTTP_CLIENT_TIMEOUT: Long = 60000

val networkModule = module {

    single {
        OkHttpClient.Builder()
            .connectTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
            .callTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(STUDIO_GHIBLI_BASE_URL)
            .build()
    }

    single {
        Service(get())
    }
}