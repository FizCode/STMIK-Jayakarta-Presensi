package com.example.stmikjayakartapresensi.di

import com.example.stmikjayakartapresensi.data.api.auth.AuthAPI
import com.example.stmikjayakartapresensi.data.local.auth.UserDAO
import com.example.stmikjayakartapresensi.datastore.AuthDataStoreManager
import com.example.stmikjayakartapresensi.repository.AuthRepository
import com.example.stmikjayakartapresensi.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideAuthRepository(
        authDataStoreManager: AuthDataStoreManager,
        api: AuthAPI,
        dao: UserDAO
    ): AuthRepository {
        return AuthRepository(
            authDataStore = authDataStoreManager,
            api = api,
            dao = dao
        )
    }

    @Singleton
    @Provides
    fun provideProfileRepository(
        userDAO: UserDAO
    ): ProfileRepository {
        return ProfileRepository(
            dao = userDAO
        )
    }

}