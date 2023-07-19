package com.example.stmikjayakartapresensi.data.local.auth

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "email") var email: String? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "nim") var nim: String? = null,
    @ColumnInfo(name = "major") var major: String? = null,
)