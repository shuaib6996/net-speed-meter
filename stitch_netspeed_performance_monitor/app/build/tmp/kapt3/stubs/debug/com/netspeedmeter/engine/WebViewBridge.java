package com.netspeedmeter.engine;

/**
 * Bridge between native Android and WebView JavaScript.
 * Provides methods for frontend to call native functions,
 * and pushes live data updates to the web view.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\n\u0018\u0000 >2\u00020\u0001:\u0001>B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\b\u0010\u000f\u001a\u00020\u0010H\u0007J\b\u0010\u0011\u001a\u00020\u0010H\u0007J\u0010\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\rH\u0007J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\rH\u0007J\b\u0010\u0016\u001a\u00020\rH\u0007J\b\u0010\u0017\u001a\u00020\rH\u0007J\b\u0010\u0018\u001a\u00020\rH\u0007J\u0010\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u001bH\u0007J\u000e\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\r0\u001dH\u0002J\u000e\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001dH\u0002J\b\u0010 \u001a\u00020\rH\u0007J\b\u0010!\u001a\u00020\u0010H\u0007J\b\u0010\"\u001a\u00020\u0010H\u0007J\b\u0010#\u001a\u00020\u0010H\u0007J\u0010\u0010$\u001a\u00020\r2\u0006\u0010%\u001a\u00020\rH\u0007J\u0010\u0010&\u001a\u00020\u00102\u0006\u0010\'\u001a\u00020(H\u0016J\u0006\u0010)\u001a\u00020\u0010J\u0010\u0010*\u001a\u00020\u00102\u0006\u0010+\u001a\u00020\tH\u0002J\u0016\u0010,\u001a\u00020\u00102\f\u0010-\u001a\b\u0012\u0004\u0012\u00020\r0.H\u0002J\u0016\u0010/\u001a\u00020\u00102\f\u00100\u001a\b\u0012\u0004\u0012\u00020\u001f0.H\u0002J\b\u00101\u001a\u00020\u0010H\u0007J\u0018\u00102\u001a\u00020\u00102\u0006\u0010%\u001a\u00020\r2\u0006\u00103\u001a\u00020\rH\u0007J\u0010\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\rH\u0007J\u0010\u00107\u001a\u00020\u00102\u0006\u0010\u000e\u001a\u00020\rH\u0007J\b\u00108\u001a\u00020\u0010H\u0002J\u0006\u00109\u001a\u00020\u0010J\b\u0010:\u001a\u00020\u0010H\u0007J\u0010\u0010;\u001a\u00020\u00102\u0006\u0010<\u001a\u000205H\u0007J\u0010\u0010=\u001a\u00020\u00102\u0006\u0010<\u001a\u000205H\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006?"}, d2 = {"Lcom/netspeedmeter/engine/WebViewBridge;", "Landroidx/lifecycle/DefaultLifecycleObserver;", "webView", "Landroid/webkit/WebView;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "(Landroid/webkit/WebView;Lkotlinx/coroutines/CoroutineScope;)V", "speedObserver", "Landroidx/lifecycle/Observer;", "Lcom/netspeedmeter/service/SpeedData;", "speedTestJob", "Lkotlinx/coroutines/Job;", "buildShareText", "", "jsonResult", "cancelSpeedTest", "", "clearAllHistory", "deleteDataUsageHistory", "datesJson", "deleteHistory", "timestampsJson", "exportAllSettings", "getCurrentSpeed", "getDataUsage", "getDataUsageHistory", "days", "", "getDeletedDataUsageDates", "", "getDeletedHistoryTimestamps", "", "getHistory", "getNetworkInfo", "getSelectedServer", "getServers", "loadSettings", "key", "onDestroy", "owner", "Landroidx/lifecycle/LifecycleOwner;", "pushDataUsageUpdate", "pushSpeedUpdate", "speed", "rememberDeletedDataUsageDates", "dates", "", "rememberDeletedHistoryTimestamps", "timestamps", "restartMonitorService", "saveSettings", "value", "selectServer", "", "serverId", "shareResult", "startMonitorService", "startObserving", "startSpeedTest", "toggleOverlay", "enable", "toggleService", "Companion", "app_debug"})
public final class WebViewBridge implements androidx.lifecycle.DefaultLifecycleObserver {
    @org.jetbrains.annotations.NotNull
    private final android.webkit.WebView webView = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job speedTestJob;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.Observer<com.netspeedmeter.service.SpeedData> speedObserver = null;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "NetSpeedMeter:Bridge";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String HISTORY_PREFS = "deleted_history";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String DELETED_TIMESTAMPS_KEY = "timestamps";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String DATA_USAGE_PREFS = "deleted_data_usage";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String DELETED_DATA_DATES_KEY = "dates";
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.engine.WebViewBridge.Companion Companion = null;
    
    public WebViewBridge(@org.jetbrains.annotations.NotNull
    android.webkit.WebView webView, @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineScope scope) {
        super();
    }
    
    /**
     * Start observing speed LiveData. Must be called after the WebView page has loaded.
     */
    public final void startObserving() {
    }
    
    private final java.util.Set<java.lang.Long> getDeletedHistoryTimestamps() {
        return null;
    }
    
    private final void rememberDeletedHistoryTimestamps(java.util.List<java.lang.Long> timestamps) {
    }
    
    private final java.util.Set<java.lang.String> getDeletedDataUsageDates() {
        return null;
    }
    
    private final void rememberDeletedDataUsageDates(java.util.List<java.lang.String> dates) {
    }
    
    @java.lang.Override
    public void onDestroy(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LifecycleOwner owner) {
    }
    
    /**
     * Start a full speed test with real-time progress pushed to JS.
     * JS must define: window.onSpeedTestProgress(json), window.onSpeedTestResult(json)
     */
    @android.webkit.JavascriptInterface
    public final void startSpeedTest() {
    }
    
    /**
     * Cancel an ongoing speed test.
     */
    @android.webkit.JavascriptInterface
    public final void cancelSpeedTest() {
    }
    
    /**
     * Select a speed test server by ID.
     * @param serverId e.g. "cloudflare", "google", "netflix", "azure"
     * @return true if server was found and selected
     */
    @android.webkit.JavascriptInterface
    public final boolean selectServer(@org.jetbrains.annotations.NotNull
    java.lang.String serverId) {
        return false;
    }
    
    /**
     * Get list of available servers as JSON array.
     * JS must define: window.onServerList(json)
     */
    @android.webkit.JavascriptInterface
    public final void getServers() {
    }
    
    /**
     * Get the currently selected server as JSON.
     * JS must define: window.onSelectedServer(json)
     */
    @android.webkit.JavascriptInterface
    public final void getSelectedServer() {
    }
    
    /**
     * Fetch network info (ISP, connection type, IP, location).
     * JS must define: window.onNetworkInfo(json)
     */
    @android.webkit.JavascriptInterface
    public final void getNetworkInfo() {
    }
    
    /**
     * Share speed test result via Android share sheet.
     * @param jsonResult JSON string of the result to share
     */
    @android.webkit.JavascriptInterface
    public final void shareResult(@org.jetbrains.annotations.NotNull
    java.lang.String jsonResult) {
    }
    
    @android.webkit.JavascriptInterface
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrentSpeed() {
        return null;
    }
    
    @android.webkit.JavascriptInterface
    public final void toggleOverlay(boolean enable) {
    }
    
    @android.webkit.JavascriptInterface
    public final void toggleService(boolean enable) {
    }
    
    @android.webkit.JavascriptInterface
    public final void restartMonitorService() {
    }
    
    private final void startMonitorService() {
    }
    
    @android.webkit.JavascriptInterface
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDataUsage() {
        return null;
    }
    
    @android.webkit.JavascriptInterface
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getHistory() {
        return null;
    }
    
    @android.webkit.JavascriptInterface
    public final void deleteHistory(@org.jetbrains.annotations.NotNull
    java.lang.String timestampsJson) {
    }
    
    /**
     * Fetch data usage history for the last N days from the database.
     * JS must define: window.onDataUsageHistory(json)
     */
    @android.webkit.JavascriptInterface
    public final void getDataUsageHistory(int days) {
    }
    
    @android.webkit.JavascriptInterface
    public final void deleteDataUsageHistory(@org.jetbrains.annotations.NotNull
    java.lang.String datesJson) {
    }
    
    @android.webkit.JavascriptInterface
    public final void saveSettings(@org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    java.lang.String value) {
    }
    
    @android.webkit.JavascriptInterface
    @org.jetbrains.annotations.NotNull
    public final java.lang.String loadSettings(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
        return null;
    }
    
    @android.webkit.JavascriptInterface
    @org.jetbrains.annotations.NotNull
    public final java.lang.String exportAllSettings() {
        return null;
    }
    
    private final void pushSpeedUpdate(com.netspeedmeter.service.SpeedData speed) {
    }
    
    public final void pushDataUsageUpdate() {
    }
    
    /**
     * Build a human-readable share text from a speed test result JSON.
     */
    private final java.lang.String buildShareText(java.lang.String jsonResult) {
        return null;
    }
    
    @android.webkit.JavascriptInterface
    public final void clearAllHistory() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/netspeedmeter/engine/WebViewBridge$Companion;", "", "()V", "DATA_USAGE_PREFS", "", "DELETED_DATA_DATES_KEY", "DELETED_TIMESTAMPS_KEY", "HISTORY_PREFS", "TAG", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}