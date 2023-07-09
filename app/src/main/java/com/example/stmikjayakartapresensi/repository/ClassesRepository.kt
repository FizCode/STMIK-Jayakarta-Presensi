package com.example.stmikjayakartapresensi.repository

import com.example.stmikjayakartapresensi.data.api.classes.ClassesDetailsResponse
import com.example.stmikjayakartapresensi.data.api.classes.ClassesAPI
import com.example.stmikjayakartapresensi.data.api.classes.MyPresenceStatusResponse
import com.example.stmikjayakartapresensi.data.api.classes.StudentPresenceResponse
import com.example.stmikjayakartapresensi.data.api.classes.TodayClassesResponse
import retrofit2.Response
import javax.inject.Inject

class ClassesRepository @Inject constructor(
    private val api: ClassesAPI
) {
    suspend fun postStudentPresence(token: String, studentsId: Int, classesId: Int): Response<StudentPresenceResponse> {
        return api.postStudentPresence(token = token, students_id = studentsId, classes_id = classesId)
    }
    suspend fun getTodayClass(token: String, studentsId: Int, day: String): Response<TodayClassesResponse> {
        return api.getTodayClass(token = token, students_id = studentsId, day = day)
    }

    suspend fun getClassDetails(token: String, classes_id: Int): Response<ClassesDetailsResponse> {
        return api.getClassDetails(token = token, classes_id = classes_id)
    }

    suspend fun getMyPresence(token: String, studentsId: Int, classesId: Int): Response<MyPresenceStatusResponse> {
        return api.getMyPresenceStatus(token = token, classes_id = classesId, students_id = studentsId)
    }
}