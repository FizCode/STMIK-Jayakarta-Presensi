package com.example.stmikjayakartapresensi.di

import com.example.stmikjayakartapresensi.Constant
import com.example.stmikjayakartapresensi.data.api.auth.AuthAPI
import com.example.stmikjayakartapresensi.data.api.classes.ClassesAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    @Named(Constant.Named.BASE_URL)
    fun provideBaseUrl(): String = "https://www.mediq.website/"

    @Singleton
    @Provides
    fun provideHttpLogging(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    @Named(Constant.Named.RETROFIT)
    fun provideRetrofit(
        @Named(Constant.Named.BASE_URL) baseUrl: String,
        client: OkHttpClient
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthAPI(
        @Named(Constant.Named.RETROFIT) retrofit: Retrofit
    ): AuthAPI {
        return retrofit.create(AuthAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideClassesAPI(
        @Named(Constant.Named.RETROFIT) retrofit: Retrofit
    ): ClassesAPI {
        return retrofit.create(ClassesAPI::class.java)
    }

}