package com.iamyajat.messtracker.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iamyajat.messtracker.model.Day
import com.iamyajat.messtracker.model.Meal
import com.iamyajat.messtracker.util.Converters

@Database(
    version = 2,
    entities = [Meal::class, Day::class],
)
@TypeConverters(Converters::class)
abstract class MealTrackerDatabase : RoomDatabase() {
    abstract fun getMealDao(): MealDao

    abstract fun getDayDao(): DayDao
}