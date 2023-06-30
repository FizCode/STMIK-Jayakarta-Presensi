package com.example.stmikjayakartapresensi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stmikjayakartapresensi.data.local.auth.UserDAO
import com.example.stmikjayakartapresensi.data.local.auth.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun userDAO(): UserDAO

    companion object {
        private const val DB_NAME = "student_presence.db"

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): LocalDatabase {
            return Room.databaseBuilder(context, LocalDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

//        fun destroyInstance() {
//            INSTANCE = null
//        }
    }
}