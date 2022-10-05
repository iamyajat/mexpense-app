package com.iamyajat.messtracker.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.iamyajat.messtracker.database.MealTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `days` (`id` INTEGER PRIMARY KEY NOT NULL, `enabled` INTEGER NOT NULL)")
        }
    }

    @Provides
    @Singleton
    fun provideMealDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MealTrackerDatabase::class.java,
        "meals_db.db"
    ).addMigrations(MIGRATION_1_2).build()

    @Provides
    @Singleton
    fun provideMealDao(
        db: MealTrackerDatabase
    ) = db.getMealDao()

    @Provides
    @Singleton
    fun provideDayDao(
        db: MealTrackerDatabase
    ) = db.getDayDao()
}