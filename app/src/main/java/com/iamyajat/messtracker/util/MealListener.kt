package com.iamyajat.messtracker.util

import com.iamyajat.messtracker.model.Meal

interface MealListener {
    fun onDelete(meal: Meal)
}
