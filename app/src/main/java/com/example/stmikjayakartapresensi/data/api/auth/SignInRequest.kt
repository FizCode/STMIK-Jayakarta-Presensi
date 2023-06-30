package com.example.stmikjayakartapresensi.data.api.auth

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("email"    ) var email    : String? = null,
    @SerializedName("password" ) var password : String? = null
)