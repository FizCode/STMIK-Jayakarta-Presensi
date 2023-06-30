package com.example.stmikjayakartapresensi

import androidx.datastore.preferences.core.stringPreferencesKey

object Constant {

    object PrefDatastore {
        const val PREF_NAME = "STMIK Jayakarta Presensi"
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
    }
    object Named {
        const val BASE_URL = "BASE_URL"
        const val RETROFIT = "RETROFIT"
    }
}
