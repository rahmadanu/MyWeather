package com.powerhouseai.myweather.util.date

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtil {

    private val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dayInWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        return "${days[dayInWeek - 1]}, ${month + 1}/$day/$year"
    }

    fun getLastUpdatedDate(unixTimestamp: Int?): String {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, h.m a", Locale.ENGLISH)
        return simpleDateFormat.format(unixTimestamp?.times(1000L) ?: 0)
    }
}