package com.jesperqvarfordt.listn.dagger.module

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jesperqvarfordt.listn.data.BuildConfig
import com.jesperqvarfordt.listn.data.api.SCApi
import com.jesperqvarfordt.listn.data.mapper.TrackMapper
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class SCModule {

    @Provides
    @Singleton
    fun provideTrackMapper(): TrackMapper {
        return TrackMapper(BuildConfig.CLIENT_ID)
    }

    @Provides
    @Singleton
    fun provideApi(): SCApi {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val clientIdInterceptor = Interceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()
            val url = originalHttpUrl.newBuilder().addQueryParameter("client_id", BuildConfig.CLIENT_ID).build()
            val requestBuilder = original.newBuilder().url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(clientIdInterceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
        return retrofit.create(SCApi::class.java)
    }

}