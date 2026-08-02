package com.netspeedmeter.service;

/**
 * Foreground service that continuously monitors network speed using TrafficStats.
 * Runs a loop every second, calculates download/upload speeds, and updates LiveData.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\u0018\u0000 A2\u00020\u0001:\u0001AB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0017\u001a\u00020\u0014H\u0002J\b\u0010\u0018\u001a\u00020\u0004H\u0002J\b\u0010\u0019\u001a\u00020\u0006H\u0002J \u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\b\u0010 \u001a\u00020!H\u0002J\u0018\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\u0016H\u0002J\u0018\u0010&\u001a\u00020\u001f2\u0006\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u001fH\u0002J\u0010\u0010)\u001a\u00020\u00162\u0006\u0010*\u001a\u00020\u001fH\u0002J\u001c\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00160,2\u0006\u0010-\u001a\u00020\u001fH\u0002J\u0014\u0010.\u001a\u0004\u0018\u00010/2\b\u00100\u001a\u0004\u0018\u000101H\u0016J\b\u00102\u001a\u00020!H\u0016J\b\u00103\u001a\u00020!H\u0016J\"\u00104\u001a\u00020\u00062\b\u00100\u001a\u0004\u0018\u0001012\u0006\u00105\u001a\u00020\u00062\u0006\u00106\u001a\u00020\u0006H\u0016J\u0012\u00107\u001a\u00020!2\b\u00108\u001a\u0004\u0018\u000101H\u0016J\b\u00109\u001a\u00020!H\u0002J\u0018\u0010:\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u001f2\u0006\u0010%\u001a\u00020\u0016H\u0002J\b\u0010;\u001a\u00020!H\u0002J\u0018\u0010<\u001a\u00020!2\u0006\u0010=\u001a\u00020\u00042\u0006\u0010>\u001a\u00020\u0004H\u0002J\u0010\u0010?\u001a\u00020!2\u0006\u0010@\u001a\u00020\u0014H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006B"}, d2 = {"Lcom/netspeedmeter/service/NetworkMonitorService;", "Landroid/app/Service;", "()V", "adaptiveInterval", "", "consecutiveLowActivity", "", "consecutiveRealUploadSamples", "lowPowerMode", "", "monitoringJob", "Lkotlinx/coroutines/Job;", "previousRxBytes", "previousTime", "previousTxBytes", "screenOffMode", "serviceScope", "Lkotlinx/coroutines/CoroutineScope;", "speedBuffer", "Lkotlin/collections/ArrayDeque;", "Lcom/netspeedmeter/service/SpeedData;", "unitPreference", "", "averageSpeedData", "calculateAdaptiveInterval", "calculatePing", "createNotification", "Landroid/app/Notification;", "contentTitle", "contentText", "totalKbps", "", "createNotificationChannel", "", "createSpeedBitmap", "Landroid/graphics/Bitmap;", "value", "unit", "filterAckOnlyUpload", "downloadKbps", "uploadKbps", "formatDataUsage", "mb", "formatSpeedForDisplay", "Lkotlin/Pair;", "kbps", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "flags", "startId", "onTaskRemoved", "rootIntent", "scheduleSelfRestart", "speedToKbps", "startMonitoring", "updateAdaptiveInterval", "downloadBytesDelta", "uploadBytesDelta", "updateNotification", "speed", "Companion", "app_debug"})
public final class NetworkMonitorService extends android.app.Service {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineScope serviceScope = null;
    @org.jetbrains.annotations.Nullable
    private kotlinx.coroutines.Job monitoringJob;
    private long previousRxBytes = 0L;
    private long previousTxBytes = 0L;
    private long previousTime = 0L;
    @org.jetbrains.annotations.NotNull
    private final kotlin.collections.ArrayDeque<com.netspeedmeter.service.SpeedData> speedBuffer = null;
    private boolean lowPowerMode = false;
    private boolean screenOffMode = false;
    private long adaptiveInterval = 1000L;
    private int consecutiveLowActivity = 0;
    private int consecutiveRealUploadSamples = 0;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String NOTIFICATION_CHANNEL_ID = "network_monitor_channel";
    public static final int NOTIFICATION_ID = 101;
    public static final int BUFFER_SIZE = 5;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "NetSpeedMeter:Service";
    @org.jetbrains.annotations.NotNull
    private static final androidx.lifecycle.MutableLiveData<com.netspeedmeter.service.SpeedData> speedLiveData = null;
    private static long sessionMobileBytes = 0L;
    private static long sessionWifiBytes = 0L;
    private static long lastSavedMobileBytes = 0L;
    private static long lastSavedWifiBytes = 0L;
    @org.jetbrains.annotations.NotNull
    private java.lang.String unitPreference = "kbps";
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.service.NetworkMonitorService.Companion Companion = null;
    
    public NetworkMonitorService() {
        super();
    }
    
    @java.lang.Override
    public void onCreate() {
    }
    
    @java.lang.Override
    public int onStartCommand(@org.jetbrains.annotations.Nullable
    android.content.Intent intent, int flags, int startId) {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.os.IBinder onBind(@org.jetbrains.annotations.Nullable
    android.content.Intent intent) {
        return null;
    }
    
    private final void startMonitoring() {
    }
    
    private final void updateNotification(com.netspeedmeter.service.SpeedData speed) {
    }
    
    /**
     * Convert speed value from its native unit to kbps for consistent scaling.
     */
    private final double speedToKbps(double value, java.lang.String unit) {
        return 0.0;
    }
    
    /**
     * Format speed for notification display: auto-scale b → kb → mb.
     */
    private final kotlin.Pair<java.lang.String, java.lang.String> formatSpeedForDisplay(double kbps) {
        return null;
    }
    
    /**
     * Format data usage in human-readable form.
     */
    private final java.lang.String formatDataUsage(double mb) {
        return null;
    }
    
    private final double filterAckOnlyUpload(double downloadKbps, double uploadKbps) {
        return 0.0;
    }
    
    private final com.netspeedmeter.service.SpeedData averageSpeedData() {
        return null;
    }
    
    private final int calculatePing() {
        return 0;
    }
    
    private final void createNotificationChannel() {
    }
    
    private final android.app.Notification createNotification(java.lang.String contentTitle, java.lang.String contentText, double totalKbps) {
        return null;
    }
    
    private final android.graphics.Bitmap createSpeedBitmap(java.lang.String value, java.lang.String unit) {
        return null;
    }
    
    @java.lang.Override
    public void onTaskRemoved(@org.jetbrains.annotations.Nullable
    android.content.Intent rootIntent) {
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    private final void scheduleSelfRestart() {
    }
    
    /**
     * Calculate adaptive interval based on power state and network activity.
     * Returns interval in milliseconds.
     */
    private final long calculateAdaptiveInterval() {
        return 0L;
    }
    
    /**
     * Update adaptive interval based on network activity.
     * If network is idle for consecutive cycles, increase interval to save battery.
     */
    private final void updateAdaptiveInterval(long downloadBytesDelta, long uploadBytesDelta) {
    }
    
    public static final long getLastSavedMobileBytes() {
        return 0L;
    }
    
    public static final long getLastSavedWifiBytes() {
        return 0L;
    }
    
    public static final long getSessionMobileBytes() {
        return 0L;
    }
    
    public static final long getSessionWifiBytes() {
        return 0L;
    }
    
    public static final void setLastSavedMobileBytes(long p0) {
    }
    
    public static final void setLastSavedWifiBytes(long p0) {
    }
    
    public static final void setSessionMobileBytes(long p0) {
    }
    
    public static final void setSessionWifiBytes(long p0) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R$\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u000b\u0010\u0002\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR$\u0010\u0010\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u0011\u0010\u0002\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR$\u0010\u0014\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u0015\u0010\u0002\u001a\u0004\b\u0016\u0010\r\"\u0004\b\u0017\u0010\u000fR$\u0010\u0018\u001a\u00020\n8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u0014\n\u0000\u0012\u0004\b\u0019\u0010\u0002\u001a\u0004\b\u001a\u0010\r\"\u0004\b\u001b\u0010\u000fR\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001d\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 \u00a8\u0006!"}, d2 = {"Lcom/netspeedmeter/service/NetworkMonitorService$Companion;", "", "()V", "BUFFER_SIZE", "", "NOTIFICATION_CHANNEL_ID", "", "NOTIFICATION_ID", "TAG", "lastSavedMobileBytes", "", "getLastSavedMobileBytes$annotations", "getLastSavedMobileBytes", "()J", "setLastSavedMobileBytes", "(J)V", "lastSavedWifiBytes", "getLastSavedWifiBytes$annotations", "getLastSavedWifiBytes", "setLastSavedWifiBytes", "sessionMobileBytes", "getSessionMobileBytes$annotations", "getSessionMobileBytes", "setSessionMobileBytes", "sessionWifiBytes", "getSessionWifiBytes$annotations", "getSessionWifiBytes", "setSessionWifiBytes", "speedLiveData", "Landroidx/lifecycle/MutableLiveData;", "Lcom/netspeedmeter/service/SpeedData;", "getSpeedLiveData", "()Landroidx/lifecycle/MutableLiveData;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final androidx.lifecycle.MutableLiveData<com.netspeedmeter.service.SpeedData> getSpeedLiveData() {
            return null;
        }
        
        public final long getSessionMobileBytes() {
            return 0L;
        }
        
        @kotlin.jvm.JvmStatic
        @java.lang.Deprecated
        public static void getSessionMobileBytes$annotations() {
        }
        
        public final void setSessionMobileBytes(long p0) {
        }
        
        public final long getSessionWifiBytes() {
            return 0L;
        }
        
        @kotlin.jvm.JvmStatic
        @java.lang.Deprecated
        public static void getSessionWifiBytes$annotations() {
        }
        
        public final void setSessionWifiBytes(long p0) {
        }
        
        public final long getLastSavedMobileBytes() {
            return 0L;
        }
        
        @kotlin.jvm.JvmStatic
        @java.lang.Deprecated
        public static void getLastSavedMobileBytes$annotations() {
        }
        
        public final void setLastSavedMobileBytes(long p0) {
        }
        
        public final long getLastSavedWifiBytes() {
            return 0L;
        }
        
        @kotlin.jvm.JvmStatic
        @java.lang.Deprecated
        public static void getLastSavedWifiBytes$annotations() {
        }
        
        public final void setLastSavedWifiBytes(long p0) {
        }
    }
}