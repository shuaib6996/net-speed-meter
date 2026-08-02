package com.netspeedmeter.engine

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.netspeedmeter.service.NetworkMonitorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Bridge between native Android and WebView JavaScript.
 * Provides methods for frontend to call native functions,
 * and pushes live data updates to the web view.
 */
class WebViewBridge(private val webView: WebView, private val scope: CoroutineScope) : DefaultLifecycleObserver {

    private var speedTestJob: Job? = null

    private val speedObserver = androidx.lifecycle.Observer<com.netspeedmeter.service.SpeedData> { speed ->
        Log.d(TAG, "speedObserver fired: download=${speed.downloadSpeed}, upload=${speed.uploadSpeed}")
        pushSpeedUpdate(speed)
    }

    init {
        Log.d(TAG, "WebViewBridge init: created (observation deferred until page loads)")
    }

    /**
     * Start observing speed LiveData. Must be called after the WebView page has loaded.
     */
    fun startObserving() {
        Log.d(TAG, "startObserving: registering observeForever on speedLiveData")
        NetworkMonitorService.speedLiveData.observeForever(speedObserver)
    }

    companion object {
        private const val TAG = "NetSpeedMeter:Bridge"
        private const val HISTORY_PREFS = "deleted_history"
        private const val DELETED_TIMESTAMPS_KEY = "timestamps"
        private const val DATA_USAGE_PREFS = "deleted_data_usage"
        private const val DELETED_DATA_DATES_KEY = "dates"
    }

    private fun getDeletedHistoryTimestamps(): Set<Long> {
        return webView.context
            .getSharedPreferences(HISTORY_PREFS, android.content.Context.MODE_PRIVATE)
            .getStringSet(DELETED_TIMESTAMPS_KEY, emptySet())
            .orEmpty()
            .mapNotNull { it.toLongOrNull() }
            .toSet()
    }

    private fun rememberDeletedHistoryTimestamps(timestamps: List<Long>) {
        if (timestamps.isEmpty()) return
        val prefs = webView.context.getSharedPreferences(HISTORY_PREFS, android.content.Context.MODE_PRIVATE)
        val updated = prefs.getStringSet(DELETED_TIMESTAMPS_KEY, emptySet())
            .orEmpty()
            .toMutableSet()
        timestamps.forEach { updated.add(it.toString()) }
        prefs.edit().putStringSet(DELETED_TIMESTAMPS_KEY, updated).apply()
    }

    private fun getDeletedDataUsageDates(): Set<String> {
        return webView.context
            .getSharedPreferences(DATA_USAGE_PREFS, android.content.Context.MODE_PRIVATE)
            .getStringSet(DELETED_DATA_DATES_KEY, emptySet())
            .orEmpty()
            .toSet()
    }

    private fun rememberDeletedDataUsageDates(dates: List<String>) {
        if (dates.isEmpty()) return
        val prefs = webView.context.getSharedPreferences(DATA_USAGE_PREFS, android.content.Context.MODE_PRIVATE)
        val updated = prefs.getStringSet(DELETED_DATA_DATES_KEY, emptySet())
            .orEmpty()
            .toMutableSet()
        dates.filter { it.isNotBlank() }.forEach { updated.add(it) }
        prefs.edit().putStringSet(DELETED_DATA_DATES_KEY, updated).apply()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        NetworkMonitorService.speedLiveData.removeObserver(speedObserver)
        speedTestJob?.cancel()
    }

    // ── JavaScript interface methods (called from web) ──

    /**
     * Start a full speed test with real-time progress pushed to JS.
     * JS must define: window.onSpeedTestProgress(json), window.onSpeedTestResult(json)
     */
    @JavascriptInterface
    fun startSpeedTest() {
        speedTestJob?.cancel()
        speedTestJob = scope.launch {
            SpeedTestEngine.startTest(
                context = webView.context,
                onProgress = { progress ->
                    scope.launch(Dispatchers.Main) {
                        webView.evaluateJavascript(
                            "window.onSpeedTestProgress && window.onSpeedTestProgress(${progress.toJson()})",
                            null
                        )
                    }
                },
                onComplete = { result ->
                    scope.launch(Dispatchers.Main) {
                        webView.evaluateJavascript(
                            "window.onSpeedTestResult && window.onSpeedTestResult(${result.toJson()})",
                            null
                        )
                    }
                }
            )
        }
    }

    /**
     * Cancel an ongoing speed test.
     */
    @JavascriptInterface
    fun cancelSpeedTest() {
        SpeedTestEngine.cancel()
        speedTestJob?.cancel()
        scope.launch(Dispatchers.Main) {
            webView.evaluateJavascript(
                "window.onSpeedTestCancelled && window.onSpeedTestCancelled()",
                null
            )
        }
    }

    /**
     * Select a speed test server by ID.
     * @param serverId e.g. "cloudflare", "google", "netflix", "azure"
     * @return true if server was found and selected
     */
    @JavascriptInterface
    fun selectServer(serverId: String): Boolean {
        return SpeedTestEngine.selectServer(serverId)
    }

    /**
     * Get list of available servers as JSON array.
     * JS must define: window.onServerList(json)
     */
    @JavascriptInterface
    fun getServers() {
        val serversJson = SpeedTestEngine.availableServers.joinToString(",", "[", "]") { server ->
            """{"id":"${server.id}","name":"${server.name}","location":"${server.location}"}"""
        }
        scope.launch(Dispatchers.Main) {
            webView.evaluateJavascript(
                "window.onServerList && window.onServerList($serversJson)",
                null
            )
        }
    }

    /**
     * Get the currently selected server as JSON.
     * JS must define: window.onSelectedServer(json)
     */
    @JavascriptInterface
    fun getSelectedServer() {
        val server = SpeedTestEngine.getSelectedServer()
        val json = """{"id":"${server.id}","name":"${server.name}","location":"${server.location}"}"""
        scope.launch(Dispatchers.Main) {
            webView.evaluateJavascript(
                "window.onSelectedServer && window.onSelectedServer($json)",
                null
            )
        }
    }

    /**
     * Fetch network info (ISP, connection type, IP, location).
     * JS must define: window.onNetworkInfo(json)
     */
    @JavascriptInterface
    fun getNetworkInfo() {
        scope.launch {
            val info = NetworkInfoProvider.fetch(webView.context)
            scope.launch(Dispatchers.Main) {
                webView.evaluateJavascript(
                    "window.onNetworkInfo && window.onNetworkInfo(${info.toJson()})",
                    null
                )
            }
        }
    }

    /**
     * Share speed test result via Android share sheet.
     * @param jsonResult JSON string of the result to share
     */
    @JavascriptInterface
    fun shareResult(jsonResult: String) {
        scope.launch(Dispatchers.Main) {
            try {
                val shareText = buildShareText(jsonResult)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                    putExtra(Intent.EXTRA_SUBJECT, "My Internet Speed Test Result")
                }
                webView.context.startActivity(
                    Intent.createChooser(intent, "Share Speed Test Result")
                )
            } catch (_: Exception) {
                // Sharing failed silently
            }
        }
    }

    @JavascriptInterface
    fun getCurrentSpeed(): String {
        val speed = NetworkMonitorService.speedLiveData.value ?: com.netspeedmeter.service.SpeedData()
        return speed.toJson()
    }

    @JavascriptInterface
    fun toggleOverlay(enable: Boolean) {
        scope.launch(Dispatchers.Main) {
            val ctx = webView.context
            if (enable) {
                // Check overlay permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(ctx)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${ctx.packageName}")
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    ctx.startActivity(intent)
                    return@launch
                }
                // Enable and start overlay service
                com.netspeedmeter.service.OverlayService.setEnabled(ctx, true)
                SettingsManager.setOverlayEnabledPref(ctx, true)
                val serviceIntent = Intent(ctx, com.netspeedmeter.service.OverlayService::class.java)
                ctx.startService(serviceIntent)
                Log.d(TAG, "toggleOverlay: overlay ENABLED and started")
            } else {
                // Disable and stop overlay service
                com.netspeedmeter.service.OverlayService.setEnabled(ctx, false)
                SettingsManager.setOverlayEnabledPref(ctx, false)
                val serviceIntent = Intent(ctx, com.netspeedmeter.service.OverlayService::class.java)
                ctx.stopService(serviceIntent)
                Log.d(TAG, "toggleOverlay: overlay DISABLED and stopped")
            }
        }
    }

    @JavascriptInterface
    fun toggleService(enable: Boolean) {
        scope.launch(Dispatchers.Main) {
            val ctx = webView.context
            if (enable) {
                SettingsManager.setServiceEnabledPref(ctx, true)
                startMonitorService()
                Log.d(TAG, "toggleService: service ENABLED and started")
            } else {
                SettingsManager.setServiceEnabledPref(ctx, false)
                val serviceIntent = Intent(ctx, NetworkMonitorService::class.java)
                ctx.stopService(serviceIntent)
                Log.d(TAG, "toggleService: service DISABLED and stopped")
            }
        }
    }

    @JavascriptInterface
    fun restartMonitorService() {
        scope.launch(Dispatchers.Main) {
            SettingsManager.setServiceEnabledPref(webView.context, true)
            startMonitorService()
            webView.evaluateJavascript("window.onMonitorRestarted && window.onMonitorRestarted()", null)
            Log.d(TAG, "restartMonitorService: monitor restart requested")
        }
    }

    private fun startMonitorService() {
        val ctx = webView.context
        val serviceIntent = Intent(ctx, NetworkMonitorService::class.java)
        androidx.core.content.ContextCompat.startForegroundService(ctx, serviceIntent)
    }

    @JavascriptInterface
    fun getDataUsage(): String {
        scope.launch {
            val usage = DataUsageTracker.getTodayUsage(webView.context)
            
            // Add uncommitted session bytes from service
            val uncommittedMobile = NetworkMonitorService.sessionMobileBytes - NetworkMonitorService.lastSavedMobileBytes
            val uncommittedWifi = NetworkMonitorService.sessionWifiBytes - NetworkMonitorService.lastSavedWifiBytes
            
            val bytesToMb = 1.0 / (1024 * 1024)
            val newMobile = usage.mobileData + (uncommittedMobile * bytesToMb)
            val newWifi = usage.wifiData + (uncommittedWifi * bytesToMb)
            val realUsage = usage.copy(
                mobileData = newMobile,
                wifiData = newWifi,
                totalData = newMobile + newWifi
            )
            
            scope.launch(Dispatchers.Main) {
                webView.evaluateJavascript(
                    "window.onDataUsageResult && window.onDataUsageResult(${realUsage.toJson()})",
                    null
                )
            }
        }
        return "{\"status\":\"fetching\"}"
    }

    @JavascriptInterface
    fun getHistory(): String {
        scope.launch {
            val dao = com.netspeedmeter.database.AppDatabase.getInstance(webView.context).appDao()
            val deletedTimestamps = getDeletedHistoryTimestamps()
            val history = dao.getSpeedTestHistory().first()
                .filterNot { deletedTimestamps.contains(it.timestamp) }
            val json = history.joinToString(",", "[", "]") { entity ->
                """{"timestamp":${entity.timestamp},"downloadResult":${entity.downloadResult},"uploadResult":${entity.uploadResult},"pingScore":${entity.pingScore},"jitter":${entity.jitter},"packetLoss":${entity.packetLoss},"stabilityIndex":${entity.stabilityIndex},"ispName":"${entity.ispName}","connectionType":"${entity.connectionType}","serverLocation":"${entity.serverLocation}"}"""
            }
            scope.launch(Dispatchers.Main) {
                webView.evaluateJavascript(
                    "window.onHistoryResult && window.onHistoryResult($json)",
                    null
                )
            }
        }
        return "{\"status\":\"fetching\"}"
    }

    @JavascriptInterface
    fun deleteHistory(timestampsJson: String) {
        scope.launch {
            try {
                val array = org.json.JSONArray(timestampsJson)
                val timestamps = mutableListOf<Long>()
                for (i in 0 until array.length()) {
                    timestamps.add(array.getLong(i))
                }
                val dao = com.netspeedmeter.database.AppDatabase.getInstance(webView.context).appDao()
                rememberDeletedHistoryTimestamps(timestamps)
                dao.deleteSpeedTestHistoryByTimestamps(timestamps)
                // Refresh list automatically; tombstones keep deleted rows hidden even if DB delete is delayed.
                getHistory()
                // Also push updated data usage in case test history had usage data
                pushDataUsageUpdate()
            } catch (e: Exception) {
                Log.e(TAG, "deleteHistory error", e)
            }
        }
    }

    /**
     * Fetch data usage history for the last N days from the database.
     * JS must define: window.onDataUsageHistory(json)
     */
    @JavascriptInterface
    fun getDataUsageHistory(days: Int) {
        scope.launch {
            try {
                val dao = com.netspeedmeter.database.AppDatabase.getInstance(webView.context).appDao()
                val deletedDates = getDeletedDataUsageDates()
                val history = dao.getDataUsageHistory(days).first()
                    .filterNot { deletedDates.contains(it.date) }
                    .toMutableList()
                
                // If today was deleted, do not recreate it from unsaved session bytes on refresh.
                val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
                val uncommittedMobile = NetworkMonitorService.sessionMobileBytes - NetworkMonitorService.lastSavedMobileBytes
                val uncommittedWifi = NetworkMonitorService.sessionWifiBytes - NetworkMonitorService.lastSavedWifiBytes
                
                if (!deletedDates.contains(today)) {
                    if (history.isNotEmpty() && history[0].date == today) {
                        val entity = history[0]
                        history[0] = entity.copy(
                            mobileData = entity.mobileData + uncommittedMobile,
                            wifiData = entity.wifiData + uncommittedWifi,
                            totalData = entity.totalData + uncommittedMobile + uncommittedWifi
                        )
                    } else if (uncommittedMobile > 0 || uncommittedWifi > 0) {
                        // Today not in DB yet, but we have session data
                        history.add(0, com.netspeedmeter.database.entity.DataUsageEntity(
                            date = today,
                            mobileData = uncommittedMobile,
                            wifiData = uncommittedWifi,
                            totalData = uncommittedMobile + uncommittedWifi
                        ))
                    }
                }

                val json = history.joinToString(",", "[", "]") { entity ->
                    """{"date":"${entity.date}","mobileData":${entity.mobileData},"wifiData":${entity.wifiData},"totalData":${entity.mobileData + entity.wifiData}}"""
                }
                scope.launch(Dispatchers.Main) {
                    webView.evaluateJavascript(
                        "window.onDataUsageHistory && window.onDataUsageHistory($json)",
                        null
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "getDataUsageHistory failed", e)
                scope.launch(Dispatchers.Main) {
                    webView.evaluateJavascript(
                        "window.onDataUsageHistory && window.onDataUsageHistory([])",
                        null
                    )
                }
            }
        }
        // Return immediately; result delivered via callback
    }

    @JavascriptInterface
    fun deleteDataUsageHistory(datesJson: String) {
        scope.launch {
            try {
                val array = org.json.JSONArray(datesJson)
                val dates = mutableListOf<String>()
                for (i in 0 until array.length()) {
                    dates.add(array.getString(i))
                }
                val dao = com.netspeedmeter.database.AppDatabase.getInstance(webView.context).appDao()
                rememberDeletedDataUsageDates(dates)
                dao.deleteDataUsageByDates(dates)
                // Push updated totals and refresh list
                pushDataUsageUpdate()
                scope.launch(Dispatchers.Main) {
                    webView.evaluateJavascript(
                        "window.onDataUsageDeleted && window.onDataUsageDeleted()",
                        null
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "deleteDataUsageHistory error", e)
            }
        }
    }

    @JavascriptInterface
    fun saveSettings(key: String, value: String) {
        when (key) {
            "overlayEnabled" -> toggleOverlay(value.toBoolean())
            "serviceEnabled" -> toggleService(value.toBoolean())
            "darkMode" -> SettingsManager.setDarkMode(webView.context, value.toBoolean())
            "unitPreference" -> SettingsManager.setUnitPreference(webView.context, value)
            "autoStart" -> SettingsManager.setAutoStartEnabled(webView.context, value.toBoolean())
            "dataSaver" -> SettingsManager.setDataSaverEnabled(webView.context, value.toBoolean())
        }
    }

    @JavascriptInterface
    fun loadSettings(key: String): String {
        return when (key) {
            "overlayEnabled" -> SettingsManager.isOverlayEnabled(webView.context).toString()
            "serviceEnabled" -> SettingsManager.isServiceEnabled(webView.context).toString()
            "darkMode" -> SettingsManager.isDarkMode(webView.context).toString()
            "unitPreference" -> SettingsManager.getUnitPreference(webView.context)
            "autoStart" -> SettingsManager.isAutoStartEnabled(webView.context).toString()
            "dataSaver" -> SettingsManager.isDataSaverEnabled(webView.context).toString()
            else -> ""
        }
    }

    @JavascriptInterface
    fun exportAllSettings(): String {
        return SettingsManager.exportSettings(webView.context)
    }

    // ── Push updates to web view ──

    private fun pushSpeedUpdate(speed: com.netspeedmeter.service.SpeedData) {
        Log.d(TAG, "pushSpeedUpdate: entering with speed=${speed.downloadSpeed}")
        scope.launch(Dispatchers.Main) {
            try {
                val json = speed.toJson()
                Log.d(TAG, "pushSpeedUpdate: about to evaluateJavascript, webView.url=${webView.url}")
                webView.evaluateJavascript(
                    "window.onSpeedUpdate && window.onSpeedUpdate($json)",
                    null
                )
                Log.d(TAG, "pushSpeedUpdate: evaluateJavascript completed successfully")
            } catch (e: Exception) {
                Log.e(TAG, "pushSpeedUpdate: CRASH in evaluateJavascript", e)
            }
        }
    }

    fun pushDataUsageUpdate() {
        scope.launch {
            val usage = DataUsageTracker.getTodayUsage(webView.context)
            scope.launch(Dispatchers.Main) {
                webView.evaluateJavascript(
                    "window.onDataUsageUpdate && window.onDataUsageUpdate(${usage.toJson()})",
                    null
                )
            }
        }
    }

    // ── Helpers ──

    /**
     * Build a human-readable share text from a speed test result JSON.
     */
    private fun buildShareText(jsonResult: String): String {
        return try {
            val json = org.json.JSONObject(jsonResult)
            val dl = json.optDouble("downloadResult", 0.0)
            val ul = json.optDouble("uploadResult", 0.0)
            val ping = json.optInt("pingScore", 0)
            val jitter = json.optDouble("jitter", 0.0)
            val loss = json.optDouble("packetLoss", 0.0)
            val isp = json.optString("ispName", "Unknown")
            val conn = json.optString("connectionType", "Unknown")

            buildString {
                appendLine("📶 Internet Speed Test Result")
                appendLine("━━━━━━━━━━━━━━━━━━━━")
                appendLine("⬇ Download: ${String.format("%.2f", dl)} Mbps")
                appendLine("⬆ Upload:   ${String.format("%.2f", ul)} Mbps")
                appendLine("🏓 Ping:     $ping ms")
                appendLine("📊 Jitter:   ${String.format("%.1f", jitter)} ms")
                appendLine("📉 Loss:     ${String.format("%.1f", loss)}%")
                appendLine("━━━━━━━━━━━━━━━━━━━━")
                appendLine("🌐 ISP:      $isp")
                appendLine("📡 Network:  $conn")
                appendLine()
                append("Tested with NetSpeed Meter")
            }
        } catch (_: Exception) {
            "Check out my internet speed test result!"
        }
    }
    @JavascriptInterface
    fun clearAllHistory() {
        scope.launch {
            try {
                val dao = com.netspeedmeter.database.AppDatabase.getInstance(webView.context).appDao()
                dao.clearAllDataUsage()
                dao.clearAllSpeedTestHistory()
                
                webView.context.getSharedPreferences(DATA_USAGE_PREFS, android.content.Context.MODE_PRIVATE)
                    .edit()
                    .remove(DELETED_DATA_DATES_KEY)
                    .apply()
                
                // Also reset service session bytes to zero
                NetworkMonitorService.sessionMobileBytes = 0L
                NetworkMonitorService.sessionWifiBytes = 0L
                NetworkMonitorService.lastSavedMobileBytes = 0L
                NetworkMonitorService.lastSavedWifiBytes = 0L
                
                scope.launch(Dispatchers.Main) {
                    webView.evaluateJavascript("window.onHistoryCleared && window.onHistoryCleared()", null)
                }
            } catch (e: Exception) {
                Log.e(TAG, "clearAllHistory error", e)
            }
        }
    }
}