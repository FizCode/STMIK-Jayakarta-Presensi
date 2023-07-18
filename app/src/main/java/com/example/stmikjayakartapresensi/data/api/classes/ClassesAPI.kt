package com.example.stmikjayakartapresensi.data.api.classes

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassesAPI {

    @POST("/api/students/{id}/presence")
    suspend fun postStudentPresence(
        @Header("Authorization") token: String,
        @Path("id") students_id: Int,
        @Query("classes_id") classes_id: Int
    ): Response<StudentPresenceResponse>

    @GET("api/students/{id}/today-class")
    suspend fun getTodayClass(
        @Header("Authorization") token: String,
        @Path("id") students_id: Int,
        @Query("day") day: String? = null
    ): Response<TodayClassesResponse>

    @GET("/api/students/class/{id}")
    suspend fun getClassDetails(
        @Header("Authorization") token: String,
        @Path("id") classes_id: Int
    ): Response<ClassesDetailsResponse>
    @GET("/api/students/{id}/presenceStatus")
    suspend fun getMyPresenceStatus(
        @Header("Authorization") token: String,
        @Path("id") students_id: Int,
        @Query("classes_id") classes_id: Int
    ): Response<PresenceStatusResponse>
}