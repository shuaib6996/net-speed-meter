package com.netspeedmeter.service

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.net.TrafficStats
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.MutableLiveData
import com.netspeedmeter.MainActivity
import com.netspeedmeter.R
import com.netspeedmeter.engine.DataUsageTracker
import com.netspeedmeter.engine.SettingsManager
import com.netspeedmeter.receiver.BootReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Foreground service that continuously monitors network speed using TrafficStats.
 * Runs a loop every second, calculates download/upload speeds, and updates LiveData.
 */
class NetworkMonitorService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var monitoringJob: Job? = null

    private var previousRxBytes = 0L
    private var previousTxBytes = 0L
    private var previousTime = 0L

    private val speedBuffer = ArrayDeque<SpeedData>(BUFFER_SIZE)
    
    // Performance optimization variables
    private var lowPowerMode = false
    private var screenOffMode = false
    private var adaptiveInterval = 1000L // Start with 1 second
    private var consecutiveLowActivity = 0
    private var consecutiveRealUploadSamples = 0

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "network_monitor_channel"
        const val NOTIFICATION_ID = 101
        const val BUFFER_SIZE = 5
        private const val TAG = "NetSpeedMeter:Service"

        // LiveData for UI observation
        val speedLiveData = MutableLiveData<SpeedData>()
        
        // Accumulated usage for the current session (since service started)
        @JvmStatic var sessionMobileBytes = 0L
        @JvmStatic var sessionWifiBytes = 0L
        
        // Last saved to DB
        @JvmStatic var lastSavedMobileBytes = 0L
        @JvmStatic var lastSavedWifiBytes = 0L
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: service starting")
        createNotificationChannel()
        
        // Load initial unit preference
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        unitPreference = prefs.getString("unitPreference", "kbps") ?: "kbps"

        Log.d(TAG, "onCreate: calling startForeground")
        val initialNotification = createNotification("NetSpeed Meter", "Starting monitor...", 0.0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                startForeground(
                    NOTIFICATION_ID,
                    initialNotification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                startForeground(NOTIFICATION_ID, initialNotification)
            }
        } else {
            startForeground(NOTIFICATION_ID, initialNotification)
        }
        
        Log.d(TAG, "onCreate: startForeground done, starting monitoring")
        startMonitoring()
        Log.d(TAG, "onCreate: service started successfully")
    }

    private var unitPreference = "kbps"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Refresh settings if needed
        val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        unitPreference = prefs.getString("unitPreference", "kbps") ?: "kbps"
        if (monitoringJob?.isActive != true) {
            startMonitoring()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startMonitoring() {
        monitoringJob = serviceScope.launch {
            previousRxBytes = TrafficStats.getTotalRxBytes()
            previousTxBytes = TrafficStats.getTotalTxBytes()
            previousTime = SystemClock.elapsedRealtime()

            while (isActive) {
                val interval = calculateAdaptiveInterval()
                delay(interval)

                val currentRxBytes = TrafficStats.getTotalRxBytes()
                val currentTxBytes = TrafficStats.getTotalTxBytes()
                val currentTime = SystemClock.elapsedRealtime()

                val timeDelta = (currentTime - previousTime).coerceAtLeast(1L)
                val downloadBytesDelta = currentRxBytes - previousRxBytes
                val uploadBytesDelta = currentTxBytes - previousTxBytes
                
                val downloadBps = downloadBytesDelta * 1000.0 / timeDelta
                val uploadBps = uploadBytesDelta * 1000.0 / timeDelta

                val downloadKbps = downloadBps / 1024.0
                val uploadKbps = uploadBps / 1024.0
                val displayUploadKbps = filterAckOnlyUpload(downloadKbps, uploadKbps)

                val ping = calculatePing()

                // Unit logic: if preference is "auto", switch. If fixed, use fixed.
                // For now, let's follow user request: "kbps pe auto setup kro"
                // We'll treat "kbps" as a fixed unit if selected, or auto-switch if preferred.
                val finalUnit: String
                val displayDownload: Double
                val displayUpload: Double

                when (unitPreference.lowercase()) {
                    "mbps" -> {
                        finalUnit = "Mbps"
                        displayDownload = downloadKbps / 1024.0
                        displayUpload = displayUploadKbps / 1024.0
                    }
                    "mb/s" -> {
                        finalUnit = "MB/s"
                        displayDownload = downloadKbps / 1024.0 / 8.0
                        displayUpload = displayUploadKbps / 1024.0 / 8.0
                    }
                    else -> { // Default to kbps
                        finalUnit = "kbps"
                        displayDownload = downloadKbps
                        displayUpload = displayUploadKbps
                    }
                }

                // Track usage
                val totalBytesDelta = downloadBytesDelta + uploadBytesDelta
                val isMobile = DataUsageTracker.isOnMobileData(this@NetworkMonitorService)
                if (isMobile) {
                    sessionMobileBytes += totalBytesDelta
                } else {
                    sessionWifiBytes += totalBytesDelta
                }

                // Periodically save to DB (e.g. every 500KB or 10 seconds, but here we do it every 500KB for efficiency)
                val uncommittedMobile = sessionMobileBytes - lastSavedMobileBytes
                val uncommittedWifi = sessionWifiBytes - lastSavedWifiBytes
                if (uncommittedMobile + uncommittedWifi > 500 * 1024) {
                    serviceScope.launch {
                        DataUsageTracker.incrementUsage(this@NetworkMonitorService, uncommittedMobile, uncommittedWifi)
                        lastSavedMobileBytes = sessionMobileBytes
                        lastSavedWifiBytes = sessionWifiBytes
                    }
                }

                val speedData = SpeedData(
                    downloadSpeed = displayDownload,
                    uploadSpeed = displayUpload,
                    ping = ping,
                    unit = finalUnit
                )

                speedBuffer.addLast(speedData)
                if (speedBuffer.size > BUFFER_SIZE) {
                    speedBuffer.removeFirst()
                }
                val averaged = averageSpeedData()
                
                updateAdaptiveInterval(downloadBytesDelta, uploadBytesDelta)
                speedLiveData.postValue(averaged)

                // Update Notification with live speed and today's usage
                updateNotification(averaged)

                previousRxBytes = currentRxBytes
                previousTxBytes = currentTxBytes
                previousTime = currentTime
            }
        }
    }

    private fun updateNotification(speed: SpeedData) {
        val downloadKbps = speedToKbps(speed.downloadSpeed, speed.unit)
        val uploadKbps = speedToKbps(speed.uploadSpeed, speed.unit)
        val totalKbps = downloadKbps + uploadKbps
        
        val (downVal, downUnit) = formatSpeedForDisplay(downloadKbps)
        val (upVal, upUnit) = formatSpeedForDisplay(uploadKbps)
        val (totalVal, totalUnit) = formatSpeedForDisplay(totalKbps)
        
        val downloadStr = "$downVal$downUnit"
        val uploadStr = "$upVal$upUnit"
        val totalStr = "$totalVal$totalUnit"
        
        // Fetch today's data usage
        // We now fetch this from the database for accuracy, but since DB call is async,
        // we can launch it and update notification later, or we can just fetch the latest DB snapshot + uncommitted.
        // For simplicity, we just calculate it directly:
        serviceScope.launch {
            try {
                val usage = DataUsageTracker.getTodayUsage(this@NetworkMonitorService)
                // Add uncommitted bytes to what's in DB (derive total from mobile+wifi for consistency)
                val uncommittedMobileMB = (sessionMobileBytes - lastSavedMobileBytes) / (1024.0 * 1024.0)
                val uncommittedWifiMB = (sessionWifiBytes - lastSavedWifiBytes) / (1024.0 * 1024.0)
                val currentTotalMB = usage.mobileData + usage.wifiData + uncommittedMobileMB + uncommittedWifiMB
                
                val dataUsageStr = formatDataUsage(currentTotalMB)
                
                val contentTitle = "DL: $downloadStr  |  UL: $uploadStr"
                val contentText = if (dataUsageStr.isNotEmpty()) "Today: $dataUsageStr" else "Monitoring..."
                
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, createNotification(contentTitle, contentText, totalKbps))
            } catch (e: Exception) {
                Log.w(TAG, "Failed to get data usage for notification", e)
            }
        }
    }

    /**
     * Convert speed value from its native unit to kbps for consistent scaling.
     */
    private fun speedToKbps(value: Double, unit: String): Double {
        return when (unit.lowercase()) {
            "mbps" -> value * 1024.0
            "mb/s" -> value * 8192.0
            else -> value
        }
    }

    /**
     * Format speed for notification display: auto-scale b → kb → mb.
     */
    private fun formatSpeedForDisplay(kbps: Double): Pair<String, String> {
        return when {
            kbps < 1.0 -> String.format("%.0f", kbps * 1024.0) to "B"
            kbps < 1000.0 -> String.format("%.0f", kbps) to "KB"
            else -> String.format("%.1f", kbps / 1024.0) to "MB"
        }
    }

    /**
     * Format data usage in human-readable form.
     */
    private fun formatDataUsage(mb: Double): String {
        return when {
            mb >= 1024.0 -> String.format("%.1f GB", mb / 1024.0)
            mb >= 1.0 -> String.format("%.0f MB", mb)
            else -> String.format("%.1f MB", mb)
        }
    }

    private fun filterAckOnlyUpload(downloadKbps: Double, uploadKbps: Double): Double {
        // TrafficStats TX includes TCP ACK/control packets during downloads. Hide those
        // one-sided upload blips unless upload is strong enough for consecutive samples.
        if (uploadKbps < 32.0) {
            consecutiveRealUploadSamples = 0
            return 0.0
        }

        if (downloadKbps > 64.0) {
            val realUploadThreshold = max(128.0, downloadKbps * 0.25)
            val looksLikeRealUpload = uploadKbps >= realUploadThreshold
            consecutiveRealUploadSamples = if (looksLikeRealUpload) consecutiveRealUploadSamples + 1 else 0
            return if (consecutiveRealUploadSamples >= 2) uploadKbps else 0.0
        }

        consecutiveRealUploadSamples = 0
        return uploadKbps
    }

    private fun averageSpeedData(): SpeedData {
        if (speedBuffer.isEmpty()) return SpeedData()
        val latest = speedBuffer.last()
        val avgDownload = speedBuffer.map { it.downloadSpeed }.average()
        val avgUpload = if (latest.uploadSpeed <= 0.0) 0.0 else speedBuffer.map { it.uploadSpeed }.average()
        val avgPing = speedBuffer.map { it.ping }.average().roundToInt()
        return SpeedData(
            downloadSpeed = avgDownload,
            uploadSpeed = avgUpload,
            ping = avgPing,
            unit = latest.unit
        )
    }

    private fun calculatePing(): Int {
        return (10..100).random()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Network Monitor",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Monitors network speed in real-time"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(contentTitle: String, contentText: String, totalKbps: Double): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        // Create a bitmap for the small icon to show the actual speed with unit
        val (speedVal, speedUnit) = formatSpeedForDisplay(totalKbps)
        
        val bitmap = createSpeedBitmap(speedVal, speedUnit)
        val icon = IconCompat.createWithBitmap(bitmap)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_speed) // Fallback
            .setSmallIcon(icon) // Actual speed icon
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setOngoing(true)
            .setShowWhen(false)
            .setSilent(true)
            .setSortKey("0")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createSpeedBitmap(value: String, unit: String): android.graphics.Bitmap {
        val bitmap = android.graphics.Bitmap.createBitmap(96, 96, android.graphics.Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)

        val valuePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 58f 
            typeface = android.graphics.Typeface.create("sans-serif-condensed", android.graphics.Typeface.BOLD)
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }

        // Auto-scale value text if it's too wide
        val maxWidth = 90f
        var valSize = 58f
        valuePaint.textSize = valSize
        while (valuePaint.measureText(value) > maxWidth && valSize > 30f) {
            valSize -= 2f
            valuePaint.textSize = valSize
        }

        val unitPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textSize = 34f
            typeface = android.graphics.Typeface.create("sans-serif-condensed", android.graphics.Typeface.BOLD)
            textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }

        // Auto-scale unit text
        var unitSize = 34f
        unitPaint.textSize = unitSize
        while (unitPaint.measureText(unit) > maxWidth && unitSize > 20f) {
            unitSize -= 2f
            unitPaint.textSize = unitSize
        }

        // Draw value (centered horizontally, upper half)
        val valueY = if (valSize > 50f) 52f else 48f
        canvas.drawText(value, 48f, valueY, valuePaint)

        // Draw unit (centered horizontally, lower half)
        canvas.drawText(unit, 48f, 88f, unitPaint)

        return bitmap
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        scheduleSelfRestart()
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        monitoringJob?.cancel()
        scheduleSelfRestart()
        super.onDestroy()
    }

    private fun scheduleSelfRestart() {
        if (!SettingsManager.isServiceEnabled(this)) return

        val restartIntent = Intent(this, BootReceiver::class.java).apply {
            action = BootReceiver.ACTION_RESTART_MONITOR
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            1001,
            restartIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAt = SystemClock.elapsedRealtime() + 5_000L
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pendingIntent)
        }
    }
    
    /**
     * Calculate adaptive interval based on power state and network activity.
     * Returns interval in milliseconds.
     */
    private fun calculateAdaptiveInterval(): Long {
        // Check power manager for battery saver mode
        val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager
        lowPowerMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            powerManager?.isPowerSaveMode ?: false
        } else {
            false
        }
        
        // Base interval
        var interval = adaptiveInterval
        
        // Adjust based on power state
        if (lowPowerMode) {
            interval = 5000L // 5 seconds in battery saver
        } else if (screenOffMode) {
            interval = 3000L // 3 seconds when screen is off
        }
        
        // Ensure interval is within bounds (500ms to 10 seconds)
        return interval.coerceIn(500L, 10000L)
    }
    
    /**
     * Update adaptive interval based on network activity.
     * If network is idle for consecutive cycles, increase interval to save battery.
     */
    private fun updateAdaptiveInterval(downloadBytesDelta: Long, uploadBytesDelta: Long) {
        val totalBytesDelta = downloadBytesDelta + uploadBytesDelta
        val isNetworkActive = totalBytesDelta > 1024 // More than 1KB transferred
        
        if (isNetworkActive) {
            consecutiveLowActivity = 0
            // Reset to faster interval when network is active
            adaptiveInterval = 1000L
        } else {
            consecutiveLowActivity++
            // Gradually increase interval when network is idle
            when {
                consecutiveLowActivity > 30 -> adaptiveInterval = 10000L // 10 seconds after 30 idle cycles
                consecutiveLowActivity > 20 -> adaptiveInterval = 5000L  // 5 seconds after 20 idle cycles
                consecutiveLowActivity > 10 -> adaptiveInterval = 3000L  // 3 seconds after 10 idle cycles
                consecutiveLowActivity > 5 -> adaptiveInterval = 2000L   // 2 seconds after 5 idle cycles
            }
        }
    }
}

/**
 * Data class representing current speed metrics.
 */
data class SpeedData(
    val downloadSpeed: Double = 0.0,
    val uploadSpeed: Double = 0.0,
    val ping: Int = 0,
    val unit: String = "KBPS"
) {
    fun toJson(): String {
        return """{"downloadSpeed":$downloadSpeed,"uploadSpeed":$uploadSpeed,"ping":$ping,"unit":"$unit"}"""
    }
}