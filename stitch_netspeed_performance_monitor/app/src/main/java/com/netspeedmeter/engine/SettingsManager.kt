package com.netspeedmeter.engine

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.netspeedmeter.service.NetworkMonitorService
import com.netspeedmeter.service.OverlayService

/**
 * Manages app settings and binds them to backend behavior.
 */
object SettingsManager {

    private const val PREF_SETTINGS = "settings"
    private const val KEY_OVERLAY_ENABLED = "overlay_enabled"
    private const val KEY_SERVICE_ENABLED = "service_enabled"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_UNIT_PREFERENCE = "unit_preference" // "KBPS" or "MBPS"
    private const val KEY_AUTO_START = "auto_start"
    private const val KEY_DATA_SAVER = "data_saver"

    // Default values
    private const val DEFAULT_OVERLAY_ENABLED = true
    private const val DEFAULT_SERVICE_ENABLED = true
    private const val DEFAULT_DARK_MODE = true
    private const val DEFAULT_UNIT_PREFERENCE = "MBPS"
    private const val DEFAULT_AUTO_START = true
    private const val DEFAULT_DATA_SAVER = false

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE)

    // Overlay enabled
    fun isOverlayEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_OVERLAY_ENABLED, DEFAULT_OVERLAY_ENABLED)

    fun setOverlayEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_OVERLAY_ENABLED, enabled).apply()
        OverlayService.setEnabled(context, enabled)
        if (enabled) {
            context.startService(Intent(context, OverlayService::class.java))
        } else {
            context.stopService(Intent(context, OverlayService::class.java))
        }
    }

    /** Only saves the preference without touching service lifecycle. Used by WebViewBridge. */
    fun setOverlayEnabledPref(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_OVERLAY_ENABLED, enabled).apply()
    }

    // Service enabled
    fun isServiceEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_SERVICE_ENABLED, DEFAULT_SERVICE_ENABLED)

    fun setServiceEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply()
        if (enabled) {
            ContextCompat.startForegroundService(context, Intent(context, NetworkMonitorService::class.java))
        } else {
            context.stopService(Intent(context, NetworkMonitorService::class.java))
        }
    }

    /** Only saves the preference without touching service lifecycle. Used by WebViewBridge. */
    fun setServiceEnabledPref(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply()
    }

    // Dark mode (UI only, frontend handles)
    fun isDarkMode(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_DARK_MODE, DEFAULT_DARK_MODE)

    fun setDarkMode(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply()
        // Notify frontend via JavaScript interface
    }

    // Unit preference
    fun getUnitPreference(context: Context): String =
        getPrefs(context).getString(KEY_UNIT_PREFERENCE, DEFAULT_UNIT_PREFERENCE) ?: DEFAULT_UNIT_PREFERENCE

    fun setUnitPreference(context: Context, unit: String) {
        getPrefs(context).edit().putString(KEY_UNIT_PREFERENCE, unit).apply()
    }

    // Auto start on boot
    fun isAutoStartEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_AUTO_START, DEFAULT_AUTO_START)

    fun setAutoStartEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_AUTO_START, enabled).apply()
    }

    // Data saver mode (reduce background updates)
    fun isDataSaverEnabled(context: Context): Boolean =
        getPrefs(context).getBoolean(KEY_DATA_SAVER, DEFAULT_DATA_SAVER)

    fun setDataSaverEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_DATA_SAVER, enabled).apply()
        if (enabled) {
            // Reduce update frequency (e.g., every 5 seconds instead of 1)
            // This is handled by the service
        }
    }

    // Export all settings as JSON for frontend
    fun exportSettings(context: Context): String {
        val prefs = getPrefs(context)
        return """
            {
                "overlayEnabled": ${prefs.getBoolean(KEY_OVERLAY_ENABLED, DEFAULT_OVERLAY_ENABLED)},
                "serviceEnabled": ${prefs.getBoolean(KEY_SERVICE_ENABLED, DEFAULT_SERVICE_ENABLED)},
                "darkMode": ${prefs.getBoolean(KEY_DARK_MODE, DEFAULT_DARK_MODE)},
                "unitPreference": "${prefs.getString(KEY_UNIT_PREFERENCE, DEFAULT_UNIT_PREFERENCE)}",
                "autoStart": ${prefs.getBoolean(KEY_AUTO_START, DEFAULT_AUTO_START)},
                "dataSaver": ${prefs.getBoolean(KEY_DATA_SAVER, DEFAULT_DATA_SAVER)}
            }
        """.trimIndent()
    }

    // Import settings from JSON (frontend can send)
    fun importSettings(context: Context, json: String) {
        // Simplified: parse JSON and apply (for demo, we'll just ignore)
        // In a real implementation, use a JSON parser
    }
}