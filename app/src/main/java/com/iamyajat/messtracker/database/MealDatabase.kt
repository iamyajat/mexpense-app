package com.iamyajat.messtracker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iamyajat.messtracker.util.Converters
import com.iamyajat.messtracker.model.Meal

@Database(
    entities = [Meal::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MealDatabase : RoomDatabase() {
    abstract fun getMealDao(): MealDao
}