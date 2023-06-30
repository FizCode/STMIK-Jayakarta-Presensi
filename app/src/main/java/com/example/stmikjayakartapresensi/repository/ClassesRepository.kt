package com.example.stmikjayakartapresensi.repository

import com.example.stmikjayakartapresensi.data.api.classes.ClassesAPI
import com.example.stmikjayakartapresensi.data.api.classes.MyClassesResponse
import retrofit2.Response
import javax.inject.Inject

class ClassesRepository @Inject constructor(
    private val api: ClassesAPI
) {
    suspend fun getMyClasses(token: String, studentsId: Int): Response<MyClassesResponse> {
        return api.getMyClasses(token = token, students_id = studentsId)
        }
}