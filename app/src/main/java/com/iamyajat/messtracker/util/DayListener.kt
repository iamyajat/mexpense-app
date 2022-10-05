package com.iamyajat.messtracker.util

import com.iamyajat.messtracker.model.Day

interface DayListener {
    fun onEnable(date: Int)
    fun onDisable(date: Int)
    fun getDayData(date: Int): Day
}
