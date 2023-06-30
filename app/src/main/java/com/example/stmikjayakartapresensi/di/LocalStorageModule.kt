package com.example.stmikjayakartapresensi.di

import android.content.Context
import com.example.stmikjayakartapresensi.data.local.auth.UserDAO
import com.example.stmikjayakartapresensi.database.LocalDatabase
import com.example.stmikjayakartapresensi.datastore.AuthDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalStorageModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return LocalDatabase.getInstance(context = context)
    }

    @Singleton
    @Provides
    fun provideUserDao(db: LocalDatabase): UserDAO {
        return db.userDAO()
    }


    @Singleton
    @Provides
    fun provideAuthDataStoreManager(@ApplicationContext context: Context)
            : AuthDataStoreManager {
        return AuthDataStoreManager(context = context)
    }
}