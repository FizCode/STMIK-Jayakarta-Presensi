package com.example.stmikjayakartapresensi.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.stmikjayakartapresensi.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataStoreManager @Inject constructor(private val context: Context) {
    companion object {
        val Context.dataStoreAuth: DataStore<Preferences> by preferencesDataStore(
            name = Constant.PrefDatastore.PREF_NAME,
            produceMigrations = ::sharedPreferencesMigration
        )

        private fun sharedPreferencesMigration(context: Context) =
            listOf(SharedPreferencesMigration(context, Constant.PrefDatastore.PREF_NAME))
    }

    fun getToken(): Flow<String> {
        return context.dataStoreAuth.data.map { preferences ->
            preferences[Constant.PrefDatastore.ACCESS_TOKEN].orEmpty()
        }
    }

    suspend fun setToken(value: String) {
        context.dataStoreAuth.edit { preferences ->
            preferences[Constant.PrefDatastore.ACCESS_TOKEN] = value
        }
    }
}