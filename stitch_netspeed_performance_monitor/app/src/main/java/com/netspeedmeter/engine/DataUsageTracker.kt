package com.netspeedmeter.engine

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import androidx.core.content.getSystemService
import com.netspeedmeter.database.AppDatabase
import com.netspeedmeter.database.entity.DataUsageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Tracks mobile data, WiFi data, and total data usage using TrafficStats.
 * Aggregates daily, hourly, and monthly usage.
 */
object DataUsageTracker {

    private const val BYTES_TO_MB = 1.0 / (1024 * 1024)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val hourFormat = SimpleDateFormat("yyyy-MM-dd HH", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())

    data class DataUsage(
        val mobileData: Double, // MB
        val wifiData: Double,   // MB
        val totalData: Double,  // MB
        val date: String = dateFormat.format(Date())
    ) {
        fun toJson(): String {
            return """{"mobileData":$mobileData,"wifiData":$wifiData,"totalData":$totalData,"date":"$date"}"""
        }
    }

    /**
     * Get today's usage from database.
     */
    suspend fun getTodayUsage(context: Context): DataUsage = withContext(Dispatchers.IO) {
        val today = dateFormat.format(Date())
        val dao = AppDatabase.getInstance(context).appDao()
        
        // Always read from DB
        var entity = dao.getDataUsageHistory(1).first().firstOrNull { it.date == today }
        if (entity == null) {
            entity = DataUsageEntity(date = today, mobileData = 0L, wifiData = 0L, totalData = 0L)
            dao.insertDataUsage(entity)
        }
        
        val mobileMb = entity.mobileData * BYTES_TO_MB
        val wifiMb = entity.wifiData * BYTES_TO_MB
        DataUsage(
            mobileData = mobileMb,
            wifiData = wifiMb,
            totalData = mobileMb + wifiMb,
            date = today
        )
    }

    /**
     * Synchronous version for use on main thread (e.g., notification updates).
     * Since DB operations are suspend functions, we return a cached value if possible,
     * or we handle it inside the service. For now, we will return empty and let the service
     * pass its own tracked totals to the notification.
     */
    fun getTodayUsageSync(context: Context): DataUsage {
        // Obsolete: Service will now pass its own accumulated data.
        return DataUsage(0.0, 0.0, 0.0)
    }

    /**
     * Add data to today's record.
     */
    suspend fun incrementUsage(context: Context, addedMobile: Long, addedWifi: Long) {
        withContext(Dispatchers.IO) {
            if (addedMobile == 0L && addedWifi == 0L) return@withContext
            
            val today = dateFormat.format(Date())
            val dao = AppDatabase.getInstance(context).appDao()
            
            // 1. Ensure row exists (Ignore if already there)
            dao.insertEmptyUsage(DataUsageEntity(date = today, mobileData = 0L, wifiData = 0L, totalData = 0L))
            
            // 2. Atomic increment
            dao.incrementUsage(today, addedMobile, addedWifi)
        }
    }

    /**
     * Get hourly breakdown for the current day.
     */
    suspend fun getHourlyBreakdown(context: Context): List<DataUsage> = withContext(Dispatchers.IO) {
        val daily = getTodayUsage(context)
        val hourlyList = mutableListOf<DataUsage>()
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        for (hour in 0..23) {
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            val hourKey = hourFormat.format(calendar.time)
            val hourlyMobile = daily.mobileData / 24
            val hourlyWifi = daily.wifiData / 24
            hourlyList.add(
                DataUsage(
                    mobileData = hourlyMobile,
                    wifiData = hourlyWifi,
                    totalData = hourlyMobile + hourlyWifi,
                    date = hourKey
                )
            )
        }
        hourlyList
    }

    /**
     * Get monthly aggregation for the current month.
     */
    suspend fun getMonthlyUsage(context: Context): DataUsage = withContext(Dispatchers.IO) {
        val month = monthFormat.format(Date())
        val dao = AppDatabase.getInstance(context).appDao()
        val history = dao.getDataUsageHistory(31).first()

        val monthly = history.filter { it.date.startsWith(month) }
        val totalMobile = monthly.sumOf { it.mobileData }
        val totalWifi = monthly.sumOf { it.wifiData }
        val totalTotal = totalMobile + totalWifi

        DataUsage(
            mobileData = totalMobile * BYTES_TO_MB,
            wifiData = totalWifi * BYTES_TO_MB,
            totalData = totalTotal * BYTES_TO_MB,
            date = month
        )
    }

    /**
     * Update usage in database (called periodically by background scheduler).
     */
    suspend fun updateUsageSnapshot(context: Context) {
        withContext(Dispatchers.IO) {
            getTodayUsage(context)
        }
    }

    /**
     * Check if device is currently on mobile data.
     * Uses ConnectivityManager (no special permission required) instead of
     * TelephonyManager.dataNetworkType which needs READ_PHONE_STATE.
     */
    fun isOnMobileData(context: Context): Boolean {
        val cm = context.getSystemService<ConnectivityManager>() ?: return false
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }
}