package com.iamyajat.messtracker.repository

import com.iamyajat.messtracker.database.MealDao
import com.iamyajat.messtracker.model.Meal
import java.util.*
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val mealDao: MealDao
) {
    suspend fun insertMeal(meal: Meal): Long = mealDao.insertMeal(meal)
    suspend fun getAllMeals(): List<Meal> = mealDao.getAllMeals()
    suspend fun getPeriodMeals(startDate: Date, endDate: Date): List<Meal> = mealDao.getPeriodMeals(startDate, endDate)
    suspend fun getPeriodExpense(startDate: Date, endDate: Date): Long = mealDao.getPeriodExpense(startDate, endDate)
}
