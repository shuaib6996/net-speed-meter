package com.netspeedmeter.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "speed_log",
    indices = [Index(value = ["timestamp"], name = "idx_speed_log_timestamp")]
)
data class SpeedLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val downloadSpeed: Double,
    val uploadSpeed: Double,
    val ping: Int
)