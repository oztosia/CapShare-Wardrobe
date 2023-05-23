package com.oztosia.capsharewardrobe.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateFormatter {

    companion object DateFormatter {
        fun convertFromMillisToDate(timestamp: Long): String {

            val date = LocalDateTime.ofEpochSecond(
                timestamp / 1000, 0,
                java.time.ZoneOffset.UTC
            )

            val today = LocalDate.now()
            val formattedDate: String

            if (date.toLocalDate() == today) {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                formattedDate = "dzisiaj " + date.format(formatter)
            } else {
                val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                formattedDate = date.format(formatter)
            }

            return formattedDate
        }
    }
}