package com.iamyajat.messtracker.util

import android.content.Context
import android.util.DisplayMetrics




object DialogFunctions {

    fun deleteDialog(
        context: Context,
        title: String,
        time: String,
        punctuation: String
    ) {

    }

    fun convertPixelsToDp(px: Float, context: Context): Int {
        val resources = context.resources
        val metrics: DisplayMetrics = resources.displayMetrics
        return (px / (metrics.densityDpi / 160f)).toInt()
    }
}