package com.iamyajat.messtracker.database

import androidx.room.*
import com.iamyajat.messtracker.model.Day

@Dao
interface DayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: Day): Long

    @Query("DELETE FROM days WHERE id=:id")
    suspend fun deleteDay(id: Long)

    @Query("SELECT * FROM days WHERE id == :id")
    suspend fun getDayInfo(id: Long): List<Day>

}