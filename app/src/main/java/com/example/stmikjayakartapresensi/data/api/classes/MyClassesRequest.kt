package com.example.stmikjayakartapresensi.data.api.classes

import com.google.gson.annotations.SerializedName

data class MyClassesRequest(
    @SerializedName("students_id" ) var studentsId : Int? = null
)
