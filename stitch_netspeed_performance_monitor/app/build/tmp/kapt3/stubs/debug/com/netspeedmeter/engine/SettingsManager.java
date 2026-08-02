package com.netspeedmeter.engine;

/**
 * Manages app settings and binds them to backend behavior.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0011\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0014J\u0018\u0010\u0015\u001a\n \u0017*\u0004\u0018\u00010\u00160\u00162\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u000e\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0014J\u0016\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\nJ\u000e\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010 \u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014J\u0016\u0010!\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010#\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010$\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010%\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010&\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010\'\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010(\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020\u0004J\u0016\u0010)\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010*\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/netspeedmeter/engine/SettingsManager;", "", "()V", "DEFAULT_AUTO_START", "", "DEFAULT_DARK_MODE", "DEFAULT_DATA_SAVER", "DEFAULT_OVERLAY_ENABLED", "DEFAULT_SERVICE_ENABLED", "DEFAULT_UNIT_PREFERENCE", "", "KEY_AUTO_START", "KEY_DARK_MODE", "KEY_DATA_SAVER", "KEY_OVERLAY_ENABLED", "KEY_SERVICE_ENABLED", "KEY_UNIT_PREFERENCE", "PREF_SETTINGS", "exportSettings", "context", "Landroid/content/Context;", "getPrefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "getUnitPreference", "importSettings", "", "json", "isAutoStartEnabled", "isDarkMode", "isDataSaverEnabled", "isOverlayEnabled", "isServiceEnabled", "setAutoStartEnabled", "enabled", "setDarkMode", "setDataSaverEnabled", "setOverlayEnabled", "setOverlayEnabledPref", "setServiceEnabled", "setServiceEnabledPref", "setUnitPreference", "unit", "app_debug"})
public final class SettingsManager {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String PREF_SETTINGS = "settings";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_OVERLAY_ENABLED = "overlay_enabled";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_SERVICE_ENABLED = "service_enabled";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_DARK_MODE = "dark_mode";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_UNIT_PREFERENCE = "unit_preference";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_AUTO_START = "auto_start";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String KEY_DATA_SAVER = "data_saver";
    private static final boolean DEFAULT_OVERLAY_ENABLED = true;
    private static final boolean DEFAULT_SERVICE_ENABLED = true;
    private static final boolean DEFAULT_DARK_MODE = true;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String DEFAULT_UNIT_PREFERENCE = "MBPS";
    private static final boolean DEFAULT_AUTO_START = true;
    private static final boolean DEFAULT_DATA_SAVER = false;
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.engine.SettingsManager INSTANCE = null;
    
    private SettingsManager() {
        super();
    }
    
    private final android.content.SharedPreferences getPrefs(android.content.Context context) {
        return null;
    }
    
    public final boolean isOverlayEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return false;
    }
    
    public final void setOverlayEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context, boolean enabled) {
    }
    
    /**
     * Only saves the preference without touching service lifecycle. Used by WebViewBridge.
     */
    public final void setOverlayEnabledPref(@org.jetbrains.annotations.NotNull
    android.content.Context context, boolean enabled) {
    }
    
    public final boolean isServiceEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return false;
    }
    
    public final void setServiceEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context, boolean enabled) {
    }
    
    /**
     * Only saves the preference without touching service lifecycle. Used by WebViewBridge.
     */
    public final void setServiceEnabledPref(@org.jetbrains.annotations.NotNull
    android.content.Context context, boolean enabled) {
    }
    
    public final boolean isDarkMode(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return false;
    }
    
    public final void setDarkMode(@org.jetbrains.annotations.NotNull
    android.content.Context context, boolean enabled) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getUnitPreference(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    public final void setUnitPreference(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String unit) {
    }
    
    public final boolean isAutoStartEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return false;
    }
    
    public final void setAutoStartEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context, boolean enabled) {
    }
    
    public final boolean isDataSaverEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return false;
    }
    
    public final void setDataSaverEnabled(@org.jetbrains.annotations.NotNull
    android.content.Context context, boolean enabled) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String exportSettings(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    public final void importSettings(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String json) {
    }
}