package com.iamyajat.messtracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iamyajat.messtracker.model.Meal
import java.util.*

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long

    @Query("SELECT * FROM meals ORDER BY addedOn DESC")
    suspend fun getAllMeals(): List<Meal>

    @Query("SELECT * FROM meals WHERE addedOn >= :startDate AND addedOn <= :endDate ORDER BY addedOn DESC")
    suspend fun getPeriodMeals(startDate: Date, endDate: Date): List<Meal>

    @Query("SELECT SUM(amount) FROM meals WHERE addedOn >= :startDate AND addedOn <= :endDate")
    suspend fun getPeriodExpense(startDate: Date, endDate: Date): Long

    @Query("DELETE FROM meals WHERE id=:mealId")
    suspend fun deleteMeal(mealId: Long)
}