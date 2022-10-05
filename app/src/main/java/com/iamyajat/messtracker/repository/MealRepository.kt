package com.iamyajat.messtracker.repository

import com.iamyajat.messtracker.database.DayDao
import com.iamyajat.messtracker.database.MealDao
import com.iamyajat.messtracker.model.Day
import com.iamyajat.messtracker.model.Meal
import java.util.*
import javax.inject.Inject

class MealRepository @Inject constructor(
    private val mealDao: MealDao,
    private val dayDao: DayDao
) {
    suspend fun insertMeal(meal: Meal): Long = mealDao.insertMeal(meal)
    suspend fun deleteMeal(mealId: Long) = mealDao.deleteMeal(mealId)
    suspend fun getAllMeals(): List<Meal> = mealDao.getAllMeals()
    suspend fun getPeriodMeals(startDate: Date, endDate: Date): List<Meal> =
        mealDao.getPeriodMeals(startDate, endDate)

    suspend fun getPeriodExpense(startDate: Date, endDate: Date): Long =
        mealDao.getPeriodExpense(startDate, endDate)

    suspend fun insertDay(day: Day): Long = dayDao.insertDay(day)
    suspend fun updateDay(day: Day) = dayDao.deleteDay(day.id)
    suspend fun getDayInfo(id: Long): List<Day> = dayDao.getDayInfo(id)


}
