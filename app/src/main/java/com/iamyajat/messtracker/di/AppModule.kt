package com.iamyajat.messtracker.di

import android.content.Context
import androidx.room.Room
import com.iamyajat.messtracker.database.MealDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAlarmDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MealDatabase::class.java,
        "meals_db.db"
    ).build()

    @Provides
    @Singleton
    fun provideAlarmDao(
        db: MealDatabase
    ) = db.getMealDao()
}