package com.netspeedmeter.engine;

/**
 * Schedules periodic background tasks:
 * - Every 1 second: speed updates (handled by Foreground Service)
 * - Every 5 minutes: save speed log to database
 * - Every 24 hours: update data usage summary
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0003\u000e\u000f\u0010B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0010\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0010\u0010\r\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/netspeedmeter/engine/BackgroundScheduler;", "", "()V", "CLEANUP_WORKER_TAG", "", "DATA_USAGE_WORKER_TAG", "SPEED_LOG_WORKER_TAG", "scheduleAll", "", "context", "Landroid/content/Context;", "scheduleCleanupWorker", "scheduleDataUsageWorker", "scheduleSpeedLogWorker", "CleanupWorker", "DataUsageWorker", "SpeedLogWorker", "app_debug"})
public final class BackgroundScheduler {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String SPEED_LOG_WORKER_TAG = "speed_log_worker";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String DATA_USAGE_WORKER_TAG = "data_usage_worker";
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String CLEANUP_WORKER_TAG = "cleanup_worker";
    @org.jetbrains.annotations.NotNull
    public static final com.netspeedmeter.engine.BackgroundScheduler INSTANCE = null;
    
    private BackgroundScheduler() {
        super();
    }
    
    public final void scheduleAll(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    private final void scheduleSpeedLogWorker(android.content.Context context) {
    }
    
    private final void scheduleDataUsageWorker(android.content.Context context) {
    }
    
    private final void scheduleCleanupWorker(android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2 = {"Lcom/netspeedmeter/engine/BackgroundScheduler$CleanupWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
    public static final class CleanupWorker extends androidx.work.CoroutineWorker {
        
        public CleanupWorker(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.NotNull
        androidx.work.WorkerParameters params) {
            super(null, null);
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.Nullable
        public java.lang.Object doWork(@org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2 = {"Lcom/netspeedmeter/engine/BackgroundScheduler$DataUsageWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
    public static final class DataUsageWorker extends androidx.work.CoroutineWorker {
        
        public DataUsageWorker(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.NotNull
        androidx.work.WorkerParameters params) {
            super(null, null);
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.Nullable
        public java.lang.Object doWork(@org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0011\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2 = {"Lcom/netspeedmeter/engine/BackgroundScheduler$SpeedLogWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "params", "Landroidx/work/WorkerParameters;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
    public static final class SpeedLogWorker extends androidx.work.CoroutineWorker {
        
        public SpeedLogWorker(@org.jetbrains.annotations.NotNull
        android.content.Context context, @org.jetbrains.annotations.NotNull
        androidx.work.WorkerParameters params) {
            super(null, null);
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.Nullable
        public java.lang.Object doWork(@org.jetbrains.annotations.NotNull
        kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
            return null;
        }
    }
}