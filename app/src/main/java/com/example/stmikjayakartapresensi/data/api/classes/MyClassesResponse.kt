package com.example.stmikjayakartapresensi.data.api.classes

import com.google.gson.annotations.SerializedName

data class MyClassesResponse(
    @SerializedName("status" ) var status : String?         = null,
    @SerializedName("data"   ) var data   : List<Data>      = listOf()
) {
    data class Data (

        @SerializedName("id"          ) var id         : Int?    = null,
        @SerializedName("classes_id"  ) var classesId  : Int?    = null,
        @SerializedName("students_id" ) var studentsId : Int?    = null,
        @SerializedName("createdAt"   ) var createdAt  : String? = null,
        @SerializedName("updatedAt"   ) var updatedAt  : String? = null

    )
}
