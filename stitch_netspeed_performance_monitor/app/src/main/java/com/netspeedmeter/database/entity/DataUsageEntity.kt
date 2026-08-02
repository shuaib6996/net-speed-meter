package com.netspeedmeter.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "data_usage",
    indices = [Index(value = ["date"], unique = true, name = "idx_data_usage_date")]
)
data class DataUsageEntity(
    @PrimaryKey
    val date: String, // YYYY-MM-DD
    val mobileData: Long, // bytes
    val wifiData: Long,
    val totalData: Long
)