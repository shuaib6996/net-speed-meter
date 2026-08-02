package com.netspeedmeter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.netspeedmeter.engine.BackgroundScheduler
import com.netspeedmeter.engine.SettingsManager
import com.netspeedmeter.engine.WebViewBridge
import com.netspeedmeter.service.NetworkMonitorService
import com.netspeedmeter.service.OverlayService
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "NetSpeedMeter:Main"
    }

    private lateinit var webView: WebView
    private lateinit var webBridge: WebViewBridge
    
    // Overlay permission launcher
    private val requestOverlayPermission = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Check if overlay permission was granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
            startOverlayService()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: START")
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.allowUniversalAccessFromFileURLs = true

        // Add JavaScript interface (legacy support)
        webView.addJavascriptInterface(LegacyJsInterface(this), "Android")

        // Initialize bridge early but don't start observing until page loads
        webBridge = WebViewBridge(webView, lifecycleScope)
        lifecycle.addObserver(webBridge)

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d(TAG, "onPageFinished: url=$url")
                
                // Start observing data now that page is ready
                webBridge.startObserving()
                
                // Add secondary interface name for newer code
                webView.addJavascriptInterface(webBridge, "AndroidBridge")
                
                // Notify web app that backend is ready
                lifecycleScope.launch {
                    webView.evaluateJavascript("window.backendReady && window.backendReady()", null)
                    // Inject initial speed data
                    val speed = NetworkMonitorService.speedLiveData.value
                    speed?.let {
                        webView.evaluateJavascript("window.onSpeedUpdate && window.onSpeedUpdate(${it.toJson()})", null)
                    }
                }
            }
        }

        webView.webChromeClient = WebChromeClient()

        // Load the single-page app from assets
        Log.d(TAG, "onCreate: loading index.html")
        webView.loadUrl("file:///android_asset/index.html")

        ensureCoreServicesRunning()

        // Defer: Permission onboarding handled by WebView JS bridge
        // The JS side checks first-launch and shows custom permission overlay
        // Services start only after user completes the onboarding flow
        Log.d(TAG, "onCreate: deferred — permissions handled by JS bridge")
        Log.d(TAG, "onCreate: END")
    }

    // ═══════════════════════════════════════════
    // PERMISSION BRIDGE — called from JavaScript
    // ═══════════════════════════════════════════

    /**
     * Called by JS: check which permissions are already granted.
     * Returns JSON with status of each permission.
     * JS must define: window.onPermissionStatus(json)
     */
    @SuppressLint("DefaultLocale")
    fun checkPermissionStatus() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val savedOnboardingComplete = prefs.getBoolean("onboarding_complete", false)

        val needsNotification = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        val notificationGranted = if (needsNotification) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true

        // If permission was revoked or an old install marked onboarding complete too early,
        // force onboarding to show again until the required notification permission is granted.
        val onboardingComplete = savedOnboardingComplete && (!needsNotification || notificationGranted)
        if (savedOnboardingComplete && !onboardingComplete) {
            prefs.edit().putBoolean("onboarding_complete", false).apply()
        }

        // This app tracks its own network usage with TrafficStats/service counters,
        // so Android's Usage Access permission is not required for the app to work.
        val usageStatsGranted = true

        val needsBattery = false
        val batteryOptimized = true

        val json = """
            {
                "onboardingComplete": $onboardingComplete,
                "notification": {"needed": $needsNotification, "granted": $notificationGranted},
                "battery": {"needed": $needsBattery, "granted": $batteryOptimized},
                "usageStats": {"needed": false, "granted": $usageStatsGranted}
            }
        """.trimIndent()

        Log.d(TAG, "checkPermissionStatus: $json")
        runOnUiThread {
            webView.evaluateJavascript("window.onPermissionStatus && window.onPermissionStatus($json)", null)
        }
    }

    /**
     * Called by JS: request notification permission (Android 13+).
     */
    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPostNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            webView.evaluateJavascript(
                "window.onPermissionResult && window.onPermissionResult('notification', true)",
                null
            )
        }
    }

    /**
     * Kept for older JS builds. Usage Access is no longer required in onboarding.
     */
    fun requestUsageStatsPermission() {
        webView.evaluateJavascript(
            "window.onPermissionResult && window.onPermissionResult('usageStats', true)",
            null
        )
        checkPermissionStatus()
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        } else {
            appOps.checkOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                packageName
            )
        }

        if (mode == android.app.AppOpsManager.MODE_ALLOWED) return true
        if (mode != android.app.AppOpsManager.MODE_DEFAULT) return false

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as android.app.usage.UsageStatsManager
        val now = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            android.app.usage.UsageStatsManager.INTERVAL_DAILY,
            now - 24 * 60 * 60 * 1000L,
            now
        )
        return !stats.isNullOrEmpty()
    }

    /**
     * Called by JS when user completes onboarding — start services.
     */
    fun completeOnboarding() {
        Log.d(TAG, "completeOnboarding: user finished permission flow")
        getSharedPreferences("settings", MODE_PRIVATE)
            .edit().putBoolean("onboarding_complete", true).apply()

        BackgroundScheduler.scheduleAll(this)

        androidx.core.content.ContextCompat.startForegroundService(
            this, Intent(this, NetworkMonitorService::class.java)
        )

        // Overlay permission will be requested separately from Settings panel,
        // not during onboarding — prevents blocking the UI with a dialog.
        if (OverlayService.isEnabled(this) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && Settings.canDrawOverlays(this)) {
            startOverlayService()
        }

        webView.evaluateJavascript(
            "window.onOnboardingComplete && window.onOnboardingComplete()", null
        )
    }

    private val requestPostNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        webView.evaluateJavascript(
            "window.onPermissionResult && window.onPermissionResult('notification', $isGranted)",
            null
        )
        checkPermissionStatus()
    }

    private fun checkAndRequestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            showOverlayPermissionDialog()
        } else {
            OverlayService.setEnabled(this, true)
            startOverlayService()
        }
    }

    private fun startOverlayService() {
        startService(Intent(this, OverlayService::class.java))
    }

    private fun showOverlayPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Overlay Permission Required")
            .setMessage("NetSpeed Meter needs overlay permission to show the floating speed widget.")
            .setPositiveButton("Grant Permission") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                requestOverlayPermission.launch(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        ensureCoreServicesRunning()
        // Re-check after permission dialogs/settings, including stale completed onboarding.
        webView.postDelayed({ checkPermissionStatus() }, 300)
    }

    private fun ensureCoreServicesRunning() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val onboardingComplete = prefs.getBoolean("onboarding_complete", false)
        if (!onboardingComplete || !SettingsManager.isServiceEnabled(this)) return

        BackgroundScheduler.scheduleAll(this)
        ContextCompat.startForegroundService(this, Intent(this, NetworkMonitorService::class.java))

        if (OverlayService.isEnabled(this) && (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this))) {
            startOverlayService()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup is handled by lifecycle observer
    }

    /**
     * Legacy JavaScript interface for compatibility with existing frontend.
     * Delegates to WebViewBridge.
     */
    private class LegacyJsInterface(private val activity: MainActivity) {
        @android.webkit.JavascriptInterface
        fun startSpeedTest() {
            activity.lifecycleScope.launch {
                activity.webBridge.startSpeedTest()
            }
        }

        @android.webkit.JavascriptInterface
        fun getCurrentSpeed(): String {
            return activity.webBridge.getCurrentSpeed()
        }

        @android.webkit.JavascriptInterface
        fun toggleOverlay(enable: Boolean) {
            activity.webBridge.toggleOverlay(enable)
        }

        @android.webkit.JavascriptInterface
        fun toggleService(enable: Boolean) {
            activity.webBridge.toggleService(enable)
        }

        @android.webkit.JavascriptInterface
        fun getDataUsage(): String {
            return activity.webBridge.getDataUsage()
        }

        @android.webkit.JavascriptInterface
        fun getHistory(): String {
            return activity.webBridge.getHistory()
        }

        @android.webkit.JavascriptInterface
        fun getDataUsageHistory(days: Int) {
            activity.webBridge.getDataUsageHistory(days)
        }

        @android.webkit.JavascriptInterface
        fun deleteSpeedEntry(entryId: String) {
            activity.lifecycleScope.launch {
                activity.webBridge.deleteHistory("[$entryId]")
            }
        }

        @android.webkit.JavascriptInterface
        fun deleteHistory(timestampsJson: String) {
            activity.lifecycleScope.launch {
                activity.webBridge.deleteHistory(timestampsJson)
            }
        }

        @android.webkit.JavascriptInterface
        fun deleteDataUsageHistory(datesJson: String) {
            activity.lifecycleScope.launch {
                activity.webBridge.deleteDataUsageHistory(datesJson)
            }
        }

        @android.webkit.JavascriptInterface
        fun saveSettings(key: String, value: String) {
            activity.webBridge.saveSettings(key, value)
        }

        @android.webkit.JavascriptInterface
        fun loadSettings(key: String): String {
            return activity.webBridge.loadSettings(key)
        }

        // ── Permission Onboarding Bridge ──

        @android.webkit.JavascriptInterface
        fun checkPermissionStatus() {
            activity.checkPermissionStatus()
        }

        @android.webkit.JavascriptInterface
        fun requestNotificationPermission() {
            activity.requestNotificationPermission()
        }

        @android.webkit.JavascriptInterface
        fun requestUsageStatsPermission() {
            activity.requestUsageStatsPermission()
        }

        @android.webkit.JavascriptInterface
        fun completeOnboarding() {
            activity.completeOnboarding()
        }
    }
}