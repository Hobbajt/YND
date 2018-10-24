package com.hobbajt.ynd.base.network.di

import com.google.gson.Gson
import com.hobbajt.ynd.BuildConfig
import com.hobbajt.ynd.application.Application
import com.hobbajt.ynd.base.network.InternetConnectionChecker
import com.hobbajt.ynd.base.network.InternetConnectionInterceptor
import com.hobbajt.ynd.imageslist.model.network.ImagesApi
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule
{
    @Singleton
    @Provides
    fun providesInternetConnectionChecker(application: Application): InternetConnectionChecker = InternetConnectionChecker(application)

    @Singleton
    @Provides
    fun providesOkHttpClient(application: Application): OkHttpClient
    {
        val internetConnectionChecker = InternetConnectionChecker(application)
        val internetConnectionInterceptor = InternetConnectionInterceptor(internetConnectionChecker)
        return OkHttpClient.Builder()
                .addInterceptor(internetConnectionInterceptor)
                .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesRequestInterface(retrofit: Retrofit): ImagesApi = retrofit.create(ImagesApi::class.java)

    @Singleton
    @Provides
    fun providesGson(): Gson = Gson()
}