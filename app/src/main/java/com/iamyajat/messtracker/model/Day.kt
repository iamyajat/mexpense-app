package com.iamyajat.messtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "days")
data class Day(
    @PrimaryKey(autoGenerate = false)
    var id: Long,
    val enabled: Boolean = false
)