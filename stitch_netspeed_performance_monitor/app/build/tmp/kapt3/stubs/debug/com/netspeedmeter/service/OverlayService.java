package com.netspeedmeter.service;

/**
 * Floating overlay service that shows real-time speed on top of other apps.
 * Uses SYSTEM_ALERT_WINDOW permission.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u0018\u0000 62\u00020\u0001:\u00016B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u001d\u001a\u00020\u0006H\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0002J\u001c\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\"0!2\u0006\u0010#\u001a\u00020\u000fH\u0002J\b\u0010$\u001a\u00020%H\u0002J\u0014\u0010&\u001a\u0004\u0018\u00010\'2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\b\u0010*\u001a\u00020\u001fH\u0016J\b\u0010+\u001a\u00020\u001fH\u0016J\"\u0010,\u001a\u00020\u00042\b\u0010(\u001a\u0004\u0018\u00010)2\u0006\u0010-\u001a\u00020\u00042\u0006\u0010.\u001a\u00020\u0004H\u0016J\u0018\u0010/\u001a\u00020\u000f2\u0006\u00100\u001a\u00020\u000f2\u0006\u00101\u001a\u00020\"H\u0002J\b\u00102\u001a\u00020\u001fH\u0002J\b\u00103\u001a\u00020\u001fH\u0002J\u0010\u00104\u001a\u00020\u001f2\u0006\u00105\u001a\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0017X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00067"}, d2 = {"Lcom/netspeedmeter/service/OverlayService;", "Landroid/app/Service;", "()V", "consecutiveStableReadings", "", "currentUpdateInterval", "", "handler", "Landroid/os/Handler;", "initialTouchX", "", "initialTouchY", "initialX", "initialY", "lastDownloadSpeed", "", "lastUploadSpeed", "overlayView", "Landroid/view/View;", "speedObserver", "Landroidx/lifecycle/Observer;", "Lcom/netspeedmeter/service/SpeedData;", "unitText", "Landroid/widget/TextView;", "updateRunnable", "Ljava/lang/Runnable;", "valueText", "windowManager", "Landroid/view/WindowManager;", "calculateUpdateInterval", "createOverlayView", "", "formatSpeedAuto", "Lkotlin/Pair;", "", "kbps", "hasOverlayPermission", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onCreate", "onDestroy", "onStartCommand", "flags", "startId", "speedToKbps", "value", "unit", "startObservingSpeed", "updateSpeed", "updateSpeedViews", "speed", "Companion", "app_debug"})
public final class OverlayService extends android.app.Service {
    private android.view.WindowManager windowManager;
    private android.view.View overlayView;
    private android.widget.TextView valueText;
    private android.widget.TextView unitText;
    private int initialX = 0;
    private int initialY = 0;
    private float initialTouchX = 0.0F;
    private float initialTouchY = 0.0F;
    @org.jetbrains.annotations.NotNull
    private final android.os.Handler handler = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.Runnable updateRunnable = null;
    private double lastDownloadSpeed = 0.0;
    private double lastUploadSpeed = 0.0;
    private int consecutiveStableReadings = 0;
    private long currentUpdateInterval = 1000L;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PREF_OVERLAY_ENABLED = "overlay_enabled";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PREF_OVERLAY_X = "overlay_x";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PREF_OVERLAY_Y = "overlay_y";
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.Observer<com.netspeedmeter.service.SpeedData> speedObserver = null;
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.service.OverlayService.Companion Companion = null;
    
    public OverlayService() {
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
    
    private final void createOverlayView() {
    }
    
    private final void startObservingSpeed() {
    }
    
    private final void updateSpeed() {
    }
    
    /**
     * Calculate adaptive update interval based on speed stability.
     * Returns interval in milliseconds.
     */
    private final long calculateUpdateInterval() {
        return 0L;
    }
    
    private final void updateSpeedViews(com.netspeedmeter.service.SpeedData speed) {
    }
    
    /**
     * Convert speed value from its native unit to kbps for consistent scaling.
     */
    private final double speedToKbps(double value, java.lang.String unit) {
        return 0.0;
    }
    
    /**
     * Auto-scale speed for display: b → kb → mb.
     * Returns (formattedValue, unitSuffix) e.g. ("775", "b"), ("110", "kb"), ("10.5", "mb")
     */
    private final kotlin.Pair<java.lang.String, java.lang.String> formatSpeedAuto(double kbps) {
        return null;
    }
    
    private final boolean hasOverlayPermission() {
        return false;
    }
    
    @java.lang.Override
    public void onDestroy() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bJ\u001e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\tJ\u0016\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\rR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/netspeedmeter/service/OverlayService$Companion;", "", "()V", "PREF_OVERLAY_ENABLED", "", "PREF_OVERLAY_X", "PREF_OVERLAY_Y", "getPosition", "Lkotlin/Pair;", "", "context", "Landroid/content/Context;", "isEnabled", "", "savePosition", "", "x", "y", "setEnabled", "enabled", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        public final boolean isEnabled(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return false;
        }
        
        public final void setEnabled(@org.jetbrains.annotations.NotNull
        android.content.Context context, boolean enabled) {
        }
        
        public final void savePosition(@org.jetbrains.annotations.NotNull
        android.content.Context context, int x, int y) {
        }
        
        @org.jetbrains.annotations.NotNull
        public final kotlin.Pair<java.lang.Integer, java.lang.Integer> getPosition(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}