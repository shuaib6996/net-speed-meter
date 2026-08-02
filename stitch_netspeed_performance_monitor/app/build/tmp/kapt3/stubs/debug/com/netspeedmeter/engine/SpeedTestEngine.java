package com.netspeedmeter.engine;

/**
 * Engine for performing real speed tests (download, upload, ping, jitter, packet loss).
 * Uses actual HTTP downloads and measures bytes transferred directly rather than
 * relying on TrafficStats (which counts ALL device traffic and inflates results).
 *
 * Test stages: ping → download → upload → complete
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0003CDEB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004H\u0002J\u0016\u0010\u0019\u001a\u00020\u00162\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u000eH\u0002J0\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u001f\u001a\u00020\u00162\u0006\u0010 \u001a\u00020\u0016H\u0002J\u0006\u0010!\u001a\u00020\"J\u0018\u0010#\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020&H\u0002J\u0006\u0010\'\u001a\u00020\u000fJQ\u0010(\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\u000b26\u0010)\u001a2\u0012\u0013\u0012\u00110\u0016\u00a2\u0006\f\b+\u0012\b\b,\u0012\u0004\b\b(-\u0012\u0013\u0012\u00110.\u00a2\u0006\f\b+\u0012\b\b,\u0012\u0004\b\b(/\u0012\u0004\u0012\u00020\"0*H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100J\\\u00101\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u0016022\b\b\u0002\u00103\u001a\u00020\u000b26\u0010)\u001a2\u0012\u0013\u0012\u00110\u0006\u00a2\u0006\f\b+\u0012\b\b,\u0012\u0004\b\b(4\u0012\u0013\u0012\u00110.\u00a2\u0006\f\b+\u0012\b\b,\u0012\u0004\b\b(/\u0012\u0004\u0012\u00020\"0*H\u0002JQ\u00105\u001a\u00020\u00162\u0006\u0010$\u001a\u00020\u000b26\u0010)\u001a2\u0012\u0013\u0012\u00110\u0016\u00a2\u0006\f\b+\u0012\b\b,\u0012\u0004\b\b(-\u0012\u0013\u0012\u00110.\u00a2\u0006\f\b+\u0012\b\b,\u0012\u0004\b\b(/\u0012\u0004\u0012\u00020\"0*H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00100J\u0010\u00106\u001a\u00020\u00162\u0006\u00107\u001a\u00020\u0016H\u0002J\u000e\u00108\u001a\u0002092\u0006\u0010:\u001a\u00020\u000bJC\u0010;\u001a\u00020\"2\u0006\u0010<\u001a\u00020=2\u0014\b\u0002\u0010)\u001a\u000e\u0012\u0004\u0012\u00020?\u0012\u0004\u0012\u00020\"0>2\u0012\u0010@\u001a\u000e\u0012\u0004\u0012\u00020A\u0012\u0004\u0012\u00020\"0>H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010BR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006F"}, d2 = {"Lcom/netspeedmeter/engine/SpeedTestEngine;", "", "()V", "DOWNLOAD_WARMUP_MS", "", "PARALLEL_CONNECTIONS", "", "PING_COUNT", "PING_TIMEOUT_MS", "SAMPLE_INTERVAL_MS", "TAG", "", "UPLOAD_SIZE_BYTES", "availableServers", "", "Lcom/netspeedmeter/engine/SpeedTestEngine$SpeedTestServer;", "getAvailableServers", "()Ljava/util/List;", "cancelled", "Ljava/util/concurrent/atomic/AtomicBoolean;", "selectedServer", "bytesPerIntervalToMbps", "", "bytes", "intervalMs", "calculateJitter", "pings", "calculateStability", "ping", "jitter", "packetLoss", "download", "upload", "cancel", "", "downloadAndCount", "url", "bytesCounter", "Ljava/util/concurrent/atomic/AtomicLong;", "getSelectedServer", "measureDownloadSpeed", "onProgress", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "speedMbps", "", "progress", "(Ljava/lang/String;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "measurePingAndJitter", "Lkotlin/Triple;", "host", "pingMs", "measureUploadSpeed", "roundToTwoDecimals", "value", "selectServer", "", "serverId", "startTest", "context", "Landroid/content/Context;", "Lkotlin/Function1;", "Lcom/netspeedmeter/engine/SpeedTestEngine$SpeedTestProgress;", "onComplete", "Lcom/netspeedmeter/engine/SpeedTestEngine$SpeedTestResult;", "(Landroid/content/Context;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "SpeedTestProgress", "SpeedTestResult", "SpeedTestServer", "app_debug"})
public final class SpeedTestEngine {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String TAG = "SpeedTestEngine";
    @org.jetbrains.annotations.NotNull
    private static final java.util.List<com.netspeedmeter.engine.SpeedTestEngine.SpeedTestServer> availableServers = null;
    @org.jetbrains.annotations.NotNull
    private static com.netspeedmeter.engine.SpeedTestEngine.SpeedTestServer selectedServer;
    @kotlin.jvm.Volatile
    @org.jetbrains.annotations.NotNull
    private static volatile java.util.concurrent.atomic.AtomicBoolean cancelled;
    private static final int PARALLEL_CONNECTIONS = 1;
    private static final int PING_COUNT = 10;
    private static final int PING_TIMEOUT_MS = 2000;
    private static final long DOWNLOAD_WARMUP_MS = 1000L;
    private static final long SAMPLE_INTERVAL_MS = 500L;
    private static final int UPLOAD_SIZE_BYTES = 8000000;
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.engine.SpeedTestEngine INSTANCE = null;
    
    private SpeedTestEngine() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.netspeedmeter.engine.SpeedTestEngine.SpeedTestServer> getAvailableServers() {
        return null;
    }
    
    public final boolean selectServer(@org.jetbrains.annotations.NotNull
    java.lang.String serverId) {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.netspeedmeter.engine.SpeedTestEngine.SpeedTestServer getSelectedServer() {
        return null;
    }
    
    public final void cancel() {
    }
    
    /**
     * Start a full speed test: Ping → Download → Upload.
     * Uses actual byte counting from HTTP responses (not TrafficStats).
     */
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object startTest(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.netspeedmeter.engine.SpeedTestEngine.SpeedTestProgress, kotlin.Unit> onProgress, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.netspeedmeter.engine.SpeedTestEngine.SpeedTestResult, kotlin.Unit> onComplete, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Measure ping, jitter, and packet loss using TCP socket connections.
     */
    private final kotlin.Triple<java.lang.Integer, java.lang.Double, java.lang.Double> measurePingAndJitter(java.lang.String host, kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.lang.Float, kotlin.Unit> onProgress) {
        return null;
    }
    
    private final double calculateJitter(java.util.List<java.lang.Long> pings) {
        return 0.0;
    }
    
    /**
     * Measure download speed by counting actual bytes received from HTTP response.
     * Uses parallel connections for accuracy. Does NOT use TrafficStats.
     */
    private final java.lang.Object measureDownloadSpeed(java.lang.String url, kotlin.jvm.functions.Function2<? super java.lang.Double, ? super java.lang.Float, kotlin.Unit> onProgress, kotlin.coroutines.Continuation<? super java.lang.Double> $completion) {
        return null;
    }
    
    /**
     * Download from URL and count bytes in a shared atomic counter.
     * This ensures we only count test traffic, not other device traffic.
     */
    private final long downloadAndCount(java.lang.String url, java.util.concurrent.atomic.AtomicLong bytesCounter) {
        return 0L;
    }
    
    /**
     * Measure upload speed by sending data and counting actual bytes transmitted.
     * Uses actual HTTP upload to the server. Does NOT use TrafficStats.
     */
    private final java.lang.Object measureUploadSpeed(java.lang.String url, kotlin.jvm.functions.Function2<? super java.lang.Double, ? super java.lang.Float, kotlin.Unit> onProgress, kotlin.coroutines.Continuation<? super java.lang.Double> $completion) {
        return null;
    }
    
    /**
     * Calculate stability index (0-1) based on ping, jitter, packet loss, and speeds.
     */
    private final double calculateStability(int ping, double jitter, double packetLoss, double download, double upload) {
        return 0.0;
    }
    
    private final double bytesPerIntervalToMbps(long bytes, long intervalMs) {
        return 0.0;
    }
    
    private final double roundToTwoDecimals(double value) {
        return 0.0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\b\u0018\u00002\u00020\u0001B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\tH\u00c6\u0003J1\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001b\u001a\u00020\tH\u00d6\u0001J\u0006\u0010\u001c\u001a\u00020\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001e"}, d2 = {"Lcom/netspeedmeter/engine/SpeedTestEngine$SpeedTestProgress;", "", "stage", "", "progress", "", "currentSpeed", "", "pingMs", "", "(Ljava/lang/String;FDI)V", "getCurrentSpeed", "()D", "getPingMs", "()I", "getProgress", "()F", "getStage", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toJson", "toString", "app_debug"})
    public static final class SpeedTestProgress {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String stage = null;
        private final float progress = 0.0F;
        private final double currentSpeed = 0.0;
        private final int pingMs = 0;
        
        public SpeedTestProgress(@org.jetbrains.annotations.NotNull
        java.lang.String stage, float progress, double currentSpeed, int pingMs) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getStage() {
            return null;
        }
        
        public final float getProgress() {
            return 0.0F;
        }
        
        public final double getCurrentSpeed() {
            return 0.0;
        }
        
        public final int getPingMs() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String toJson() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component1() {
            return null;
        }
        
        public final float component2() {
            return 0.0F;
        }
        
        public final double component3() {
            return 0.0;
        }
        
        public final int component4() {
            return 0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.netspeedmeter.engine.SpeedTestEngine.SpeedTestProgress copy(@org.jetbrains.annotations.NotNull
        java.lang.String stage, float progress, double currentSpeed, int pingMs) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u001d\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\b\u0018\u00002\u00020\u0001B_\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u000bH\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010 \u001a\u00020\u0006H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u000bH\u00c6\u0003J\t\u0010%\u001a\u00020\u000bH\u00c6\u0003J\t\u0010&\u001a\u00020\u000bH\u00c6\u0003Jo\u0010\'\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000bH\u00c6\u0001J\u0013\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010+\u001a\u00020\u0006H\u00d6\u0001J\u0006\u0010,\u001a\u00020\u000bJ\t\u0010-\u001a\u00020\u000bH\u00d6\u0001R\u0011\u0010\f\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\r\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011R\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0013\u00a8\u0006."}, d2 = {"Lcom/netspeedmeter/engine/SpeedTestEngine$SpeedTestResult;", "", "downloadResult", "", "uploadResult", "pingScore", "", "jitter", "packetLoss", "stabilityIndex", "ispName", "", "connectionType", "serverLocation", "error", "(DDIDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getConnectionType", "()Ljava/lang/String;", "getDownloadResult", "()D", "getError", "getIspName", "getJitter", "getPacketLoss", "getPingScore", "()I", "getServerLocation", "getStabilityIndex", "getUploadResult", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toJson", "toString", "app_debug"})
    public static final class SpeedTestResult {
        private final double downloadResult = 0.0;
        private final double uploadResult = 0.0;
        private final int pingScore = 0;
        private final double jitter = 0.0;
        private final double packetLoss = 0.0;
        private final double stabilityIndex = 0.0;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String ispName = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String connectionType = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String serverLocation = null;
        @org.jetbrains.annotations.Nullable
        private final java.lang.String error = null;
        
        public SpeedTestResult(double downloadResult, double uploadResult, int pingScore, double jitter, double packetLoss, double stabilityIndex, @org.jetbrains.annotations.NotNull
        java.lang.String ispName, @org.jetbrains.annotations.NotNull
        java.lang.String connectionType, @org.jetbrains.annotations.NotNull
        java.lang.String serverLocation, @org.jetbrains.annotations.Nullable
        java.lang.String error) {
            super();
        }
        
        public final double getDownloadResult() {
            return 0.0;
        }
        
        public final double getUploadResult() {
            return 0.0;
        }
        
        public final int getPingScore() {
            return 0;
        }
        
        public final double getJitter() {
            return 0.0;
        }
        
        public final double getPacketLoss() {
            return 0.0;
        }
        
        public final double getStabilityIndex() {
            return 0.0;
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
        public final java.lang.String getServerLocation() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getError() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String toJson() {
            return null;
        }
        
        public final double component1() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String component10() {
            return null;
        }
        
        public final double component2() {
            return 0.0;
        }
        
        public final int component3() {
            return 0;
        }
        
        public final double component4() {
            return 0.0;
        }
        
        public final double component5() {
            return 0.0;
        }
        
        public final double component6() {
            return 0.0;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component7() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component8() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String component9() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.netspeedmeter.engine.SpeedTestEngine.SpeedTestResult copy(double downloadResult, double uploadResult, int pingScore, double jitter, double packetLoss, double stabilityIndex, @org.jetbrains.annotations.NotNull
        java.lang.String ispName, @org.jetbrains.annotations.NotNull
        java.lang.String connectionType, @org.jetbrains.annotations.NotNull
        java.lang.String serverLocation, @org.jetbrains.annotations.Nullable
        java.lang.String error) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003JE\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000b\u00a8\u0006\u001e"}, d2 = {"Lcom/netspeedmeter/engine/SpeedTestEngine$SpeedTestServer;", "", "id", "", "name", "location", "downloadUrl", "uploadUrl", "pingHost", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDownloadUrl", "()Ljava/lang/String;", "getId", "getLocation", "getName", "getPingHost", "getUploadUrl", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    public static final class SpeedTestServer {
        @org.jetbrains.annotations.NotNull
        private final java.lang.String id = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String name = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String location = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String downloadUrl = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String uploadUrl = null;
        @org.jetbrains.annotations.NotNull
        private final java.lang.String pingHost = null;
        
        public SpeedTestServer(@org.jetbrains.annotations.NotNull
        java.lang.String id, @org.jetbrains.annotations.NotNull
        java.lang.String name, @org.jetbrains.annotations.NotNull
        java.lang.String location, @org.jetbrains.annotations.NotNull
        java.lang.String downloadUrl, @org.jetbrains.annotations.NotNull
        java.lang.String uploadUrl, @org.jetbrains.annotations.NotNull
        java.lang.String pingHost) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getId() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getLocation() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getDownloadUrl() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getUploadUrl() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.lang.String getPingHost() {
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
        public final java.lang.String component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.netspeedmeter.engine.SpeedTestEngine.SpeedTestServer copy(@org.jetbrains.annotations.NotNull
        java.lang.String id, @org.jetbrains.annotations.NotNull
        java.lang.String name, @org.jetbrains.annotations.NotNull
        java.lang.String location, @org.jetbrains.annotations.NotNull
        java.lang.String downloadUrl, @org.jetbrains.annotations.NotNull
        java.lang.String uploadUrl, @org.jetbrains.annotations.NotNull
        java.lang.String pingHost) {
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