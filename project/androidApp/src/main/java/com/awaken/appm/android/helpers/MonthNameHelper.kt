package com.awaken.appm.android.helpers

import java.util.Calendar
import java.util.Locale

class MonthNameHelper {

    val months = mapOf(
        "January" to "Январь",
        "February" to "Февраль",
        "March" to "Март",
        "April" to "Апрель",
        "May" to "Май",
        "June" to "Июнь",
        "July" to "Июль",
        "August" to "Август",
        "September" to "Сентябрь",
        "October" to "Октябрь",
        "November" to "Ноябрь",
        "December" to "Декабрь"
    )

    fun getCurrentMonthName():String{
        val calendar: Calendar = Calendar.getInstance()
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US)

        return if(monthName != null) {
            months.getOrDefault(monthName, "Новый лист")
        } else {
            "Новый лист"
        }
    }

    fun getMonthByName(monthName:String) : String{
        return months.getOrDefault(monthName, "Новый лист")
    }
}