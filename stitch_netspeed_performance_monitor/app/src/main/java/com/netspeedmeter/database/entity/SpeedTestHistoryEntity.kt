package com.netspeedmeter.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speed_test_history")
data class SpeedTestHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val downloadResult: Double, // Mbps
    val uploadResult: Double, // Mbps
    val pingScore: Int, // ms
    val jitter: Double, // ms - standard deviation of ping times
    val packetLoss: Double, // 0-100 percentage
    val stabilityIndex: Double, // 0-1
    val ispName: String = "", // ISP name
    val connectionType: String = "", // WiFi, 4G, 5G, etc.
    val serverLocation: String = "" // test server location
)