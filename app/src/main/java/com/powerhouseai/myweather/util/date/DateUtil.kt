package com.powerhouseai.myweather.util.date

import java.util.Calendar

object DateUtil {

    private val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dayInWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        return "${days[dayInWeek]}, $month/$day/$year"
    }
}