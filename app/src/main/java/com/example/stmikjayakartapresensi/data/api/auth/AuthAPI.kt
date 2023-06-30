package com.example.stmikjayakartapresensi.data.api.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthAPI {
    @POST("api/students/login")
    suspend fun signIn(@Body request: SignInRequest): Response<SignInResponse>

    @GET("api/students/who-am-i")
    suspend fun getUser(
        @Header("Authorization") token: String,
    ): Response<GetUserResponse>
}