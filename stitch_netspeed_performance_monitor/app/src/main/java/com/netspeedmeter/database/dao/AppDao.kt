package com.netspeedmeter.database.dao

import androidx.room.*
import com.netspeedmeter.database.entity.DataUsageEntity
import com.netspeedmeter.database.entity.SpeedLogEntity
import com.netspeedmeter.database.entity.SpeedTestHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // SpeedLog operations
    @Query("SELECT * FROM speed_log ORDER BY timestamp DESC LIMIT :limit")
    fun getSpeedLogs(limit: Int = 100): Flow<List<SpeedLogEntity>>

    @Insert
    suspend fun insertSpeedLog(log: SpeedLogEntity)

    @Query("DELETE FROM speed_log WHERE timestamp < :olderThan")
    suspend fun deleteOldSpeedLogs(olderThan: Long)

    // DataUsage operations
    @Query("SELECT * FROM data_usage WHERE date = :date")
    suspend fun getDataUsage(date: String): DataUsageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataUsage(usage: DataUsageEntity)

    @Query("SELECT * FROM data_usage ORDER BY date DESC LIMIT :limit")
    fun getDataUsageHistory(limit: Int = 30): Flow<List<DataUsageEntity>>

    // SpeedTestHistory operations
    @Query("SELECT * FROM speed_test_history ORDER BY timestamp DESC")
    fun getSpeedTestHistory(): Flow<List<SpeedTestHistoryEntity>>

    @Insert
    suspend fun insertSpeedTestHistory(history: SpeedTestHistoryEntity)

    @Query("DELETE FROM speed_test_history WHERE timestamp < :olderThan")
    suspend fun deleteOldSpeedTestHistory(olderThan: Long)

    @Query("DELETE FROM speed_test_history WHERE timestamp IN (:timestamps)")
    suspend fun deleteSpeedTestHistoryByTimestamps(timestamps: List<Long>)

    @Query("DELETE FROM data_usage WHERE date IN (:dates)")
    suspend fun deleteDataUsageByDates(dates: List<String>)

    @Query("UPDATE data_usage SET mobileData = mobileData + :mobile, wifiData = wifiData + :wifi, totalData = totalData + :mobile + :wifi WHERE date = :date")
    suspend fun incrementUsage(date: String, mobile: Long, wifi: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmptyUsage(entity: DataUsageEntity)

    @Query("DELETE FROM data_usage")
    suspend fun clearAllDataUsage()

    @Query("DELETE FROM speed_test_history")
    suspend fun clearAllSpeedTestHistory()
}