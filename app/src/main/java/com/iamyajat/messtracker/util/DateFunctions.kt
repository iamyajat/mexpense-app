package com.iamyajat.messtracker.util

import com.iamyajat.messtracker.util.Constants.BREAKFAST
import com.iamyajat.messtracker.util.Constants.DINNER
import com.iamyajat.messtracker.util.Constants.LATE_DINNER
import com.iamyajat.messtracker.util.Constants.LUNCH
import com.iamyajat.messtracker.util.Constants.SNACKS
import java.text.SimpleDateFormat
import java.util.*

object DateFunctions {

    fun getTodayStart(): Date {
        val c = Calendar.getInstance()

        c[Calendar.HOUR_OF_DAY] = c.getActualMinimum(Calendar.HOUR_OF_DAY)
        c[Calendar.MINUTE] = c.getActualMinimum(Calendar.MINUTE)
        c[Calendar.SECOND] = c.getActualMinimum(Calendar.SECOND)

        return Date(c.timeInMillis)
    }

    fun getTodayEnd(): Date {
        val c = Calendar.getInstance()

        c[Calendar.HOUR_OF_DAY] = c.getActualMaximum(Calendar.HOUR_OF_DAY)
        c[Calendar.MINUTE] = c.getActualMaximum(Calendar.MINUTE)
        c[Calendar.SECOND] = c.getActualMaximum(Calendar.SECOND)

        return Date(c.timeInMillis)
    }

    fun getMonthStart(): Date {
        val c = Calendar.getInstance()

        c[Calendar.DAY_OF_MONTH] = c.getActualMinimum(Calendar.DAY_OF_MONTH)
        c[Calendar.HOUR_OF_DAY] = c.getActualMinimum(Calendar.HOUR_OF_DAY)
        c[Calendar.MINUTE] = c.getActualMinimum(Calendar.MINUTE)
        c[Calendar.SECOND] = c.getActualMinimum(Calendar.SECOND)

        return Date(c.timeInMillis)
    }

    fun getMonthEnd(): Date {
        val c = Calendar.getInstance()

        c[Calendar.DAY_OF_MONTH] = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        c[Calendar.HOUR_OF_DAY] = c.getActualMaximum(Calendar.HOUR_OF_DAY)
        c[Calendar.MINUTE] = c.getActualMaximum(Calendar.MINUTE)
        c[Calendar.SECOND] = c.getActualMaximum(Calendar.SECOND)

        return Date(c.timeInMillis)
    }

    fun formatDateTime(date: Date, dateAndTime: Boolean = true): String {
        if (dateAndTime) {
            return SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH).format(date)
        }
        return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(date)
    }

    fun formatDate(date: Date): String {
        return SimpleDateFormat("dd/MM/yy", Locale.ENGLISH).format(date)
    }

    fun getRemainingDays(): Int {
        val c = Calendar.getInstance()
        return c.getActualMaximum(Calendar.DAY_OF_MONTH) - c[Calendar.DAY_OF_MONTH] + 1
    }

    fun getTimeOfDay(): Int {
        val c = Calendar.getInstance()
        if (c[Calendar.HOUR_OF_DAY] in 5..12) {
            return BREAKFAST
        }
        if (c[Calendar.HOUR_OF_DAY] in 12..16) {
            return LUNCH
        }
        if (c[Calendar.HOUR_OF_DAY] in 16..18) {
            return SNACKS
        }
        if (c[Calendar.HOUR_OF_DAY] in 18..21) {
            return DINNER
        }
        return LATE_DINNER
    }

    fun getMealType(): String {
        when (getTimeOfDay()) {
            BREAKFAST -> return "Breakfast"
            LUNCH -> return "Lunch"
            SNACKS -> return "Snacks"
            DINNER -> return "Dinner"
        }
        return "Dinner"
    }

    fun getGreeting(): String {
        when (getTimeOfDay()) {
            BREAKFAST -> return "Good morning"
            LUNCH -> return "Good afternoon"
            SNACKS -> return "Good evening"
            DINNER -> return "Good evening"
        }
        return "Good night"
    }
}