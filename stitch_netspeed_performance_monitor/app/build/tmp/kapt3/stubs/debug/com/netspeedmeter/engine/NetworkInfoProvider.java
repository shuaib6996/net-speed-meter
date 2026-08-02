package com.netspeedmeter.engine;

/**
 * Provides network information: ISP name, connection type, external IP,
 * and approximate server location. Used to enrich speed test results.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0001\u0011B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0019\u0010\f\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0012"}, d2 = {"Lcom/netspeedmeter/engine/NetworkInfoProvider;", "", "()V", "client", "Lokhttp3/OkHttpClient;", "detectCellularGeneration", "", "context", "Landroid/content/Context;", "detectCellularGenerationFallback", "detectConnectionType", "detectIspName", "fetch", "Lcom/netspeedmeter/engine/NetworkInfoProvider$NetworkInfo;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getNetworkIcon", "connectionType", "NetworkInfo", "app_debug"})
public final class NetworkInfoProvider {
    @org.jetbrains.annotations.NotNull
    private static final okhttp3.OkHttpClient client = null;
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.engine.NetworkInfoProvider INSTANCE = null;
    
    private NetworkInfoProvider() {
        super();
    }
    
    /**
     * Fetch network info. ISP and connection type come from device APIs;
     * external IP and location come from public web APIs.
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object fetch(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.netspeedmeter.engine.NetworkInfoProvider.NetworkInfo> $completion) {
        return null;
    }
    
    /**
     * Detect connection type: WiFi, 5G, 4G, 3G, 2G, Ethernet, or Unknown.
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String detectConnectionType(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
    
    /**
     * Detect cellular network generation: 5G, 4G/LTE, 3G, 2G.
     * Wrapped in try-catch because dataNetworkType requires READ_PHONE_STATE
     * permission which we don't declare in the manifest.
     */
    private final java.lang.String detectCellularGeneration(android.content.Context context) {
        return null;
    }
    
    /**
     * Fallback cellular detection using ConnectivityManager when
     * TelephonyManager.dataNetworkType is inaccessible.
     */
    private final java.lang.String detectCellularGenerationFallback(android.content.Context context) {
        return null;
    }
    
    /**
     * Attempt to detect ISP name from SIM carrier or network operator.
     */
    private final java.lang.String detectIspName(android.content.Context context) {
        return null;
    }
    
    /**
     * Get Material icon name for the connection type.
     */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getNetworkIcon(@org.jetbrains.annotations.NotNull
    java.lang.String connectionType) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\bJ\t\u0010\u000f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J;\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0018\u001a\u00020\u0019H\u00d6\u0001J\u0006\u0010\u001a\u001a\u00020\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u00a8\u0006\u001c"}, d2 = {"Lcom/netspeedmeter/engine/NetworkInfoProvider$NetworkInfo;", "", "ispName", "", "connectionType", "externalIp", "serverLocation", "networkTypeIcon", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getConnectionType", "()Ljava/lang/String;", "getExternalIp", "getIspName", "getNetworkTypeIcon", "getServerLocation", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toJson", "toString", "app_debug"})
    public static final class NetworkInfo {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String ispName = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String connectionType = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String externalIp = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String serverLocation = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String networkTypeIcon = null;
        
        public NetworkInfo(@org.jetbrains.annotations.NotNull
        java.lang.String ispName, @org.jetbrains.annotations.NotNull
        java.lang.String connectionType, @org.jetbrains.annotations.NotNull
        java.lang.String externalIp, @org.jetbrains.annotations.NotNull
        java.lang.String serverLocation, @org.jetbrains.annotations.NotNull
        java.lang.String networkTypeIcon) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getIspName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getConnectionType() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getExternalIp() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getServerLocation() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getNetworkTypeIcon() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String toJson() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.netspeedmeter.engine.NetworkInfoProvider.NetworkInfo copy(@org.jetbrains.annotations.NotNull
        java.lang.String ispName, @org.jetbrains.annotations.NotNull
        java.lang.String connectionType, @org.jetbrains.annotations.NotNull
        java.lang.String externalIp, @org.jetbrains.annotations.NotNull
        java.lang.String serverLocation, @org.jetbrains.annotations.NotNull
        java.lang.String networkTypeIcon) {
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