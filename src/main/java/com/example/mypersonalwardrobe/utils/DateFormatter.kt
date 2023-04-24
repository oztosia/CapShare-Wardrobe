package com.example.mypersonalwardrobe.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateFormatter {

    companion object DateFormatter {
        fun convertFromMillisToDate(timestamp: Long): String {

            val date = LocalDateTime.ofEpochSecond(
                timestamp / 1000, 0,
                java.time.ZoneOffset.UTC
            )
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm")
            val formattedDate = date.format(formatter)

            return  formattedDate.toString()
        }
    }
}