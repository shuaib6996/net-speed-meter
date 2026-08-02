package com.netspeedmeter.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.netspeedmeter.R
import kotlin.math.abs

/**
 * Floating overlay service that shows real-time speed on top of other apps.
 * Uses SYSTEM_ALERT_WINDOW permission.
 */
class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var valueText: TextView
    private lateinit var unitText: TextView

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateSpeed()
            // Adaptive update interval based on network activity
            val interval = calculateUpdateInterval()
            handler.postDelayed(this, interval)
        }
    }
    
    // Performance optimization variables
    private var lastDownloadSpeed = 0.0
    private var lastUploadSpeed = 0.0
    private var consecutiveStableReadings = 0
    private var currentUpdateInterval = 1000L // Start with 1 second

    companion object {
        private const val PREF_OVERLAY_ENABLED = "overlay_enabled"
        private const val PREF_OVERLAY_X = "overlay_x"
        private const val PREF_OVERLAY_Y = "overlay_y"

        fun isEnabled(context: Context): Boolean {
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            return prefs.getBoolean(PREF_OVERLAY_ENABLED, false)
        }

        fun setEnabled(context: Context, enabled: Boolean) {
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            prefs.edit().putBoolean(PREF_OVERLAY_ENABLED, enabled).apply()
        }

        fun savePosition(context: Context, x: Int, y: Int) {
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            prefs.edit().putInt(PREF_OVERLAY_X, x).putInt(PREF_OVERLAY_Y, y).apply()
        }

        fun getPosition(context: Context): Pair<Int, Int> {
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val x = prefs.getInt(PREF_OVERLAY_X, 100)
            val y = prefs.getInt(PREF_OVERLAY_Y, 100)
            return Pair(x, y)
        }
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        // Check if we have overlay permission
        if (hasOverlayPermission()) {
            createOverlayView()
            startObservingSpeed()
            handler.post(updateRunnable)
        } else {
            // No permission, show toast and stop service
            Toast.makeText(
                this,
                "Overlay permission required for floating widget",
                Toast.LENGTH_LONG
            ).show()
            stopSelf()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If overlay is disabled, stop self
        if (!isEnabled(this)) {
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createOverlayView() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.overlay_speed, null)

        valueText = overlayView.findViewById(R.id.overlay_value)
        unitText = overlayView.findViewById(R.id.overlay_unit)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )

        // Default to top-left next to clock, shifted right precisely, below status bar
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 315
        params.y = 24
        // Make overlay draggable and save position on release
        overlayView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = (initialX + (event.rawX - initialTouchX)).toInt()
                    params.y = (initialY + (event.rawY - initialTouchY)).toInt()
                    windowManager.updateViewLayout(overlayView, params)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    savePosition(this, params.x, params.y)
                    true
                }
                else -> false
            }
        }

        windowManager.addView(overlayView, params)
    }

    private fun startObservingSpeed() {
        NetworkMonitorService.speedLiveData.observeForever(speedObserver)
    }

    private val speedObserver = Observer<SpeedData> { speed ->
        updateSpeedViews(speed)
    }

    private fun updateSpeed() {
        // Ensure we have latest speed from LiveData
        val speed = NetworkMonitorService.speedLiveData.value ?: return
        
        // Check if speed has changed significantly
        val downloadChanged = abs(speed.downloadSpeed - lastDownloadSpeed) > 1.0
        val uploadChanged = abs(speed.uploadSpeed - lastUploadSpeed) > 1.0
        
        if (downloadChanged || uploadChanged) {
            // Speed changed significantly, reset stability counter
            consecutiveStableReadings = 0
            currentUpdateInterval = 1000L
        } else {
            // Speed is stable, increase counter
            consecutiveStableReadings++
            if (consecutiveStableReadings > 10) {
                consecutiveStableReadings = 10 // Cap at 10
            }
        }
        
        // Update last speeds
        lastDownloadSpeed = speed.downloadSpeed
        lastUploadSpeed = speed.uploadSpeed
        
        updateSpeedViews(speed)
    }
    
    /**
     * Calculate adaptive update interval based on speed stability.
     * Returns interval in milliseconds.
     */
    private fun calculateUpdateInterval(): Long {
        // If speeds are stable (not changing much), increase interval to save battery
        return if (consecutiveStableReadings > 5) {
            3000L // 3 seconds when stable
        } else if (consecutiveStableReadings > 2) {
            2000L // 2 seconds when somewhat stable
        } else {
            1000L // 1 second when changing
        }
    }

    private fun updateSpeedViews(speed: SpeedData) {
        handler.post {
            val totalKbps = speedToKbps(speed.downloadSpeed + speed.uploadSpeed, speed.unit)
            val (displayValue, displayUnit) = formatSpeedAuto(totalKbps)
            valueText.text = displayValue
            unitText.text = displayUnit
        }
    }

    /**
     * Convert speed value from its native unit to kbps for consistent scaling.
     */
    private fun speedToKbps(value: Double, unit: String): Double {
        return when (unit.lowercase()) {
            "mbps" -> value * 1024.0
            "mb/s" -> value * 8192.0   // MB/s → kbps (×1024×8)
            else -> value               // already kbps
        }
    }

    /**
     * Auto-scale speed for display: b → kb → mb.
     * Returns (formattedValue, unitSuffix) e.g. ("775", "b"), ("110", "kb"), ("10.5", "mb")
     */
    private fun formatSpeedAuto(kbps: Double): Pair<String, String> {
        return when {
            kbps < 1.0 -> String.format("%.0f", kbps * 1024.0) to "b"
            kbps < 1000.0 -> String.format("%.0f", kbps) to "kb"
            else -> String.format("%.1f", kbps / 1024.0) to "mb"
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            // Before Android M, no runtime permission required
            true
        }
    }

    override fun onDestroy() {
        handler.removeCallbacks(updateRunnable)
        NetworkMonitorService.speedLiveData.removeObserver(speedObserver)
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
        super.onDestroy()
    }
}