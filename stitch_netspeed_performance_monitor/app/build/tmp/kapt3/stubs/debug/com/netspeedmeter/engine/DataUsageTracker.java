package com.netspeedmeter.engine;

/**
 * Tracks mobile data, WiFi data, and total data usage using TrafficStats.
 * Aggregates daily, hourly, and monthly usage.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u001bB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ)\u0010\u0012\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\f\u001a\u00020\rJ\u0019\u0010\u001a\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\rH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001c"}, d2 = {"Lcom/netspeedmeter/engine/DataUsageTracker;", "", "()V", "BYTES_TO_MB", "", "dateFormat", "Ljava/text/SimpleDateFormat;", "hourFormat", "monthFormat", "getHourlyBreakdown", "", "Lcom/netspeedmeter/engine/DataUsageTracker$DataUsage;", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMonthlyUsage", "getTodayUsage", "getTodayUsageSync", "incrementUsage", "", "addedMobile", "", "addedWifi", "(Landroid/content/Context;JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isOnMobileData", "", "updateUsageSnapshot", "DataUsage", "app_debug"})
public final class DataUsageTracker {
    private static final double BYTES_TO_MB = 9.5367431640625E-7;
    @org.jetbrains.annotations.NotNull
    private static final java.text.SimpleDateFormat dateFormat = null;
    @org.jetbrains.annotations.NotNull
    private static final java.text.SimpleDateFormat hourFormat = null;
    @org.jetbrains.annotations.NotNull
    private static final java.text.SimpleDateFormat monthFormat = null;
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.engine.DataUsageTracker INSTANCE = null;
    
    private DataUsageTracker() {
        super();
    }
    
    /**
     * Get today's usage from database.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getTodayUsage(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.netspeedmeter.engine.DataUsageTracker.DataUsage> $completion) {
        return null;
    }
    
    /**
     * Synchronous version for use on main thread (e.g., notification updates).
     * Since DB operations are suspend functions, we return a cached value if possible,
     * or we handle it inside the service. For now, we will return empty and let the service
     * pass its own tracked totals to the notification.
     */
    @org.jetbrains.annotations.NotNull
    public final com.netspeedmeter.engine.DataUsageTracker.DataUsage getTodayUsageSync(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    /**
     * Add data to today's record.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object incrementUsage(@org.jetbrains.annotations.NotNull
    android.content.Context context, long addedMobile, long addedWifi, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Get hourly breakdown for the current day.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getHourlyBreakdown(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.netspeedmeter.engine.DataUsageTracker.DataUsage>> $completion) {
        return null;
    }
    
    /**
     * Get monthly aggregation for the current month.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object getMonthlyUsage(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.netspeedmeter.engine.DataUsageTracker.DataUsage> $completion) {
        return null;
    }
    
    /**
     * Update usage in database (called periodically by background scheduler).
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateUsageSnapshot(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Check if device is currently on mobile data.
     * Uses ConnectivityManager (no special permission required) instead of
     * TelephonyManager.dataNetworkType which needs READ_PHONE_STATE.
     */
    public final boolean isOnMobileData(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B\'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\u0006\u0010\u0019\u001a\u00020\u0007J\t\u0010\u001a\u001a\u00020\u0007H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\f\u00a8\u0006\u001b"}, d2 = {"Lcom/netspeedmeter/engine/DataUsageTracker$DataUsage;", "", "mobileData", "", "wifiData", "totalData", "date", "", "(DDDLjava/lang/String;)V", "getDate", "()Ljava/lang/String;", "getMobileData", "()D", "getTotalData", "getWifiData", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toJson", "toString", "app_debug"})
    public static final class DataUsage {
        private final double mobileData = 0.0;
        private final double wifiData = 0.0;
        private final double totalData = 0.0;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String date = null;
        
        public DataUsage(double mobileData, double wifiData, double totalData, @org.jetbrains.annotations.NotNull
        java.lang.String date) {
            super();
        }
        
        public final double getMobileData() {
            return 0.0;
        }
        
        public final double getWifiData() {
            return 0.0;
        }
        
        public final double getTotalData() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getDate() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String toJson() {
            return null;
        }
        
        public final double component1() {
            return 0.0;
        }
        
        public final double component2() {
            return 0.0;
        }
        
        public final double component3() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.netspeedmeter.engine.DataUsageTracker.DataUsage copy(double mobileData, double wifiData, double totalData, @org.jetbrains.annotations.NotNull
        java.lang.String date) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
}