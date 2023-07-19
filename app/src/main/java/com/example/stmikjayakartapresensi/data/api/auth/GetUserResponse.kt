package com.example.stmikjayakartapresensi.data.api.auth

import com.google.gson.annotations.SerializedName

data class GetUserResponse (
    @SerializedName("id"        ) var id        : Int?    = null,
    @SerializedName("name"      ) var name      : String? = null,
    @SerializedName("email"     ) var email     : String? = null,
    @SerializedName("nim"       ) var nim       : String? = null,
    @SerializedName("major"     ) var major     : String? = null,
    @SerializedName("createdAt" ) var createdAt : String? = null,
    @SerializedName("updatedAt" ) var updatedAt : String? = null
)