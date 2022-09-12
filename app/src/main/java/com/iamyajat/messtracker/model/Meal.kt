package com.iamyajat.messtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val mealName: String,
    val description: String,
    val addedOn: Date,
    val amount: Long,
    val rating: Int
)