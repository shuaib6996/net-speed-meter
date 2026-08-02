package com.netspeedmeter.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.netspeedmeter.engine.BackgroundScheduler
import com.netspeedmeter.service.NetworkMonitorService
import com.netspeedmeter.service.OverlayService

/**
 * Receives BOOT_COMPLETED and starts the foreground service and overlay if needed.
 */
class BootReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_RESTART_MONITOR = "com.netspeedmeter.action.RESTART_MONITOR"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == ACTION_RESTART_MONITOR) {

            val serviceEnabled = com.netspeedmeter.engine.SettingsManager.isServiceEnabled(context)
            val isBootAction = intent.action == Intent.ACTION_BOOT_COMPLETED ||
                intent.action == "android.intent.action.QUICKBOOT_POWERON"
            val autoStartEnabled = com.netspeedmeter.engine.SettingsManager.isAutoStartEnabled(context)
            if (!serviceEnabled || (isBootAction && !autoStartEnabled)) return

            // Start foreground service
            val serviceIntent = Intent(context, NetworkMonitorService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }

            // Start overlay if enabled and permission granted
            if (OverlayService.isEnabled(context)) {
                // Check if we have overlay permission
                val hasOverlayPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(context)
                } else {
                    true // Before Android M, no runtime permission required
                }
                
                if (hasOverlayPermission) {
                    val overlayIntent = Intent(context, OverlayService::class.java)
                    context.startService(overlayIntent)
                }
                // If no permission, overlay won't start but that's OK
            }

            // Schedule background workers
            BackgroundScheduler.scheduleAll(context)
        }
    }
}