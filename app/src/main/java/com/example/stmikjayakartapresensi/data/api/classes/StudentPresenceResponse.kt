package com.example.stmikjayakartapresensi.data.api.classes

import com.google.gson.annotations.SerializedName

data class StudentPresenceResponse(
    @SerializedName("message"  ) var message  : String?   = null,
    @SerializedName("presence" ) var presence : Presence? = Presence()

) {
    data class Presence (

        @SerializedName("id"                ) var id               : Int?    = null,
        @SerializedName("students_id"       ) var studentsId       : Int?    = null,
        @SerializedName("classes_id"        ) var classesId        : Int?    = null,
        @SerializedName("datetime_presence" ) var datetimePresence : String? = null,
        @SerializedName("updatedAt"         ) var updatedAt        : String? = null,
        @SerializedName("createdAt"         ) var createdAt        : String? = null

    )
}
