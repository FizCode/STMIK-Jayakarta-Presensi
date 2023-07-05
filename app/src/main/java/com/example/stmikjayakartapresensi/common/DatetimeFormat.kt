package com.example.stmikjayakartapresensi.common

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DatetimeFormat {
    fun timeFormatter(time: String): String? {
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val parseTime =  try {
            LocalTime.parse(time, formatter)
        } catch (e: Exception) {
            null
        }

        val outputFormatter = DateTimeFormatter.ofPattern("HH.mm")
        return parseTime?.format(outputFormatter)
    }
}