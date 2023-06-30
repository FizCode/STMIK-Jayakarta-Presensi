package com.example.stmikjayakartapresensi.data.api.classes

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface ClassesAPI {
    @GET("api/students/my-classes")
    suspend fun getMyClasses(
        @Header("Authorization") token: String,
        @Body students_id: Int
    ): Response<MyClassesResponse>
}