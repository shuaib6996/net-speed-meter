package com.netspeedmeter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 \u001e2\u00020\u0001:\u0002\u001e\u001fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0007J\u0006\u0010\u0010\u001a\u00020\u000eJ\b\u0010\u0011\u001a\u00020\u000eH\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u000eH\u0016J\u0012\u0010\u0015\u001a\u00020\u000e2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0015J\b\u0010\u0018\u001a\u00020\u000eH\u0014J\b\u0010\u0019\u001a\u00020\u000eH\u0014J\u0006\u0010\u001a\u001a\u00020\u000eJ\u0006\u0010\u001b\u001a\u00020\u000eJ\b\u0010\u001c\u001a\u00020\u000eH\u0002J\b\u0010\u001d\u001a\u00020\u000eH\u0002R\u001c\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0007\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\b0\b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/netspeedmeter/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "requestOverlayPermission", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "requestPostNotificationPermission", "", "webBridge", "Lcom/netspeedmeter/engine/WebViewBridge;", "webView", "Landroid/webkit/WebView;", "checkAndRequestOverlayPermission", "", "checkPermissionStatus", "completeOnboarding", "ensureCoreServicesRunning", "hasUsageStatsPermission", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onResume", "requestNotificationPermission", "requestUsageStatsPermission", "showOverlayPermissionDialog", "startOverlayService", "Companion", "LegacyJsInterface", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "NetSpeedMeter:Main";
    private android.webkit.WebView webView;
    private com.netspeedmeter.engine.WebViewBridge webBridge;
    @org.jetbrains.annotations.NotNull
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> requestOverlayPermission = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> requestPostNotificationPermission = null;
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.MainActivity.Companion Companion = null;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override
    @android.annotation.SuppressLint(value = {"SetJavaScriptEnabled"})
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * Called by JS: check which permissions are already granted.
     * Returns JSON with status of each permission.
     * JS must define: window.onPermissionStatus(json)
     */
    @android.annotation.SuppressLint(value = {"DefaultLocale"})
    public final void checkPermissionStatus() {
    }
    
    /**
     * Called by JS: request notification permission (Android 13+).
     */
    public final void requestNotificationPermission() {
    }
    
    /**
     * Kept for older JS builds. Usage Access is no longer required in onboarding.
     */
    public final void requestUsageStatsPermission() {
    }
    
    private final boolean hasUsageStatsPermission() {
        return false;
    }
    
    /**
     * Called by JS when user completes onboarding — start services.
     */
    public final void completeOnboarding() {
    }
    
    private final void checkAndRequestOverlayPermission() {
    }
    
    private final void startOverlayService() {
    }
    
    private final void showOverlayPermissionDialog() {
    }
    
    @java.lang.Override
    protected void onResume() {
    }
    
    private final void ensureCoreServicesRunning() {
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/netspeedmeter/MainActivity$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    /**
     * Legacy JavaScript interface for compatibility with existing frontend.
     * Delegates to WebViewBridge.
     */
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0007J\b\u0010\u0007\u001a\u00020\u0006H\u0007J\u0010\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\nH\u0007J\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\nH\u0007J\b\u0010\u000f\u001a\u00020\nH\u0007J\b\u0010\u0010\u001a\u00020\nH\u0007J\u0010\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J\b\u0010\u0014\u001a\u00020\nH\u0007J\u0010\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nH\u0007J\b\u0010\u0017\u001a\u00020\u0006H\u0007J\b\u0010\u0018\u001a\u00020\u0006H\u0007J\u0018\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\n2\u0006\u0010\u001a\u001a\u00020\nH\u0007J\b\u0010\u001b\u001a\u00020\u0006H\u0007J\u0010\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u001eH\u0007J\u0010\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u001eH\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/netspeedmeter/MainActivity$LegacyJsInterface;", "", "activity", "Lcom/netspeedmeter/MainActivity;", "(Lcom/netspeedmeter/MainActivity;)V", "checkPermissionStatus", "", "completeOnboarding", "deleteDataUsageHistory", "datesJson", "", "deleteHistory", "timestampsJson", "deleteSpeedEntry", "entryId", "getCurrentSpeed", "getDataUsage", "getDataUsageHistory", "days", "", "getHistory", "loadSettings", "key", "requestNotificationPermission", "requestUsageStatsPermission", "saveSettings", "value", "startSpeedTest", "toggleOverlay", "enable", "", "toggleService", "app_debug"})
    static final class LegacyJsInterface {
        @org.jetbrains.annotations.NotNull
        private final com.netspeedmeter.MainActivity activity = null;
        
        public LegacyJsInterface(@org.jetbrains.annotations.NotNull
        com.netspeedmeter.MainActivity activity) {
            super();
        }
        
        @android.webkit.JavascriptInterface
        public final void startSpeedTest() {
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
        public final void getDataUsageHistory(int days) {
        }
        
        @android.webkit.JavascriptInterface
        public final void deleteSpeedEntry(@org.jetbrains.annotations.NotNull
        java.lang.String entryId) {
        }
        
        @android.webkit.JavascriptInterface
        public final void deleteHistory(@org.jetbrains.annotations.NotNull
        java.lang.String timestampsJson) {
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
        public final void checkPermissionStatus() {
        }
        
        @android.webkit.JavascriptInterface
        public final void requestNotificationPermission() {
        }
        
        @android.webkit.JavascriptInterface
        public final void requestUsageStatsPermission() {
        }
        
        @android.webkit.JavascriptInterface
        public final void completeOnboarding() {
        }
    }
}