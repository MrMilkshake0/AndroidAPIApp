package com.example.nit3213.di

import com.example.nit3213.data.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Hilt module that tells the app how to build and provide network-related objects.
// These @Provides functions create single instances (Singletons) that can be injected anywhere.
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Provide a single Gson instance for the whole app.
    @Provides @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    // Provide an OkHttpClient with logging (very helpful during development).
    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            // BODY logs request and response lines + their respective headers and bodies (if present).
            // Consider changing to BASIC in release builds for less noise.
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // Build a Retrofit instance configured with:
    // - Base URL (from ApiService)
    // - OkHttp client
    // - Gson converter for JSON
    @Provides @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // Create an implementation of ApiService using Retrofit.
    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}
