package com.example.stmikjayakartapresensi.data.api.classes

import com.google.gson.annotations.SerializedName

data class TodayClassesResponse(
    @SerializedName("status" ) var status : String?         = null,
    @SerializedName("data"   ) var data   : List<Data>      = listOf()
) {
    data class Data (

        @SerializedName("id"            ) var id           : Int?          = null,
        @SerializedName("classes_id"    ) var classesId    : Int?          = null,
        @SerializedName("students_id"   ) var studentsId   : Int?          = null,
        @SerializedName("createdAt"     ) var createdAt    : String?       = null,
        @SerializedName("updatedAt"     ) var updatedAt    : String?       = null,
        @SerializedName("class_details" ) var classDetails : ClassDetails? = ClassDetails()

    )
    data class ClassDetails (

        @SerializedName("id"            ) var id           : Int?     = null,
        @SerializedName("class_code"    ) var classCode    : String?  = null,
        @SerializedName("class_name"    ) var className    : String?  = null,
        @SerializedName("class_room"    ) var classRoom    : String?  = null,
        @SerializedName("lecturer_id"   ) var lecturerId   : Int?     = null,
        @SerializedName("day"           ) var day          : String?  = null,
        @SerializedName("class_started" ) var classStarted : String?  = null,
        @SerializedName("class_ended"   ) var classEnded   : String?  = null,
        @SerializedName("start_date"    ) var startDate    : String?  = null,
        @SerializedName("end_date"      ) var endDate      : String?  = null,
        @SerializedName("lecture"       ) var lecture      : Lecture? = Lecture()

    )
    data class Lecture (

        @SerializedName("id"           ) var id          : Int?    = null,
        @SerializedName("nid"          ) var nid         : String? = null,
        @SerializedName("name"         ) var name        : String? = null,
        @SerializedName("phone_number" ) var phoneNumber : String? = null

    )
}
