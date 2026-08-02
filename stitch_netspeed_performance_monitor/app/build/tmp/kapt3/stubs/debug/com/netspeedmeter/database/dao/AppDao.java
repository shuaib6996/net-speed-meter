package com.netspeedmeter.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\bg\u0018\u00002\u00020\u0001J\u0011\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0005\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004J\u001f\u0010\u0006\u001a\u00020\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u0019\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u001f\u0010\u0010\u001a\u00020\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ\u001b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u001e\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\b0\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\'J\u001e\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\b0\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\'J\u0014\u0010\u001c\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001d0\b0\u0017H\'J)\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020\r2\u0006\u0010 \u001a\u00020\rH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010!J\u0019\u0010\"\u001a\u00020\u00032\u0006\u0010#\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J\u0019\u0010%\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u0013H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J\u0019\u0010\'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\u001bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\u0019\u0010*\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u001dH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010,\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006-"}, d2 = {"Lcom/netspeedmeter/database/dao/AppDao;", "", "clearAllDataUsage", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearAllSpeedTestHistory", "deleteDataUsageByDates", "dates", "", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOldSpeedLogs", "olderThan", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOldSpeedTestHistory", "deleteSpeedTestHistoryByTimestamps", "timestamps", "getDataUsage", "Lcom/netspeedmeter/database/entity/DataUsageEntity;", "date", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDataUsageHistory", "Lkotlinx/coroutines/flow/Flow;", "limit", "", "getSpeedLogs", "Lcom/netspeedmeter/database/entity/SpeedLogEntity;", "getSpeedTestHistory", "Lcom/netspeedmeter/database/entity/SpeedTestHistoryEntity;", "incrementUsage", "mobile", "wifi", "(Ljava/lang/String;JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertDataUsage", "usage", "(Lcom/netspeedmeter/database/entity/DataUsageEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertEmptyUsage", "entity", "insertSpeedLog", "log", "(Lcom/netspeedmeter/database/entity/SpeedLogEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertSpeedTestHistory", "history", "(Lcom/netspeedmeter/database/entity/SpeedTestHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao
public abstract interface AppDao {
    
    @androidx.room.Query(value = "SELECT * FROM speed_log ORDER BY timestamp DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.netspeedmeter.database.entity.SpeedLogEntity>> getSpeedLogs(int limit);
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertSpeedLog(@org.jetbrains.annotations.NotNull
    com.netspeedmeter.database.entity.SpeedLogEntity log, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM speed_log WHERE timestamp < :olderThan")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteOldSpeedLogs(long olderThan, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM data_usage WHERE date = :date")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getDataUsage(@org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.netspeedmeter.database.entity.DataUsageEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertDataUsage(@org.jetbrains.annotations.NotNull
    com.netspeedmeter.database.entity.DataUsageEntity usage, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM data_usage ORDER BY date DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.netspeedmeter.database.entity.DataUsageEntity>> getDataUsageHistory(int limit);
    
    @androidx.room.Query(value = "SELECT * FROM speed_test_history ORDER BY timestamp DESC")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.netspeedmeter.database.entity.SpeedTestHistoryEntity>> getSpeedTestHistory();
    
    @androidx.room.Insert
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertSpeedTestHistory(@org.jetbrains.annotations.NotNull
    com.netspeedmeter.database.entity.SpeedTestHistoryEntity history, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM speed_test_history WHERE timestamp < :olderThan")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteOldSpeedTestHistory(long olderThan, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM speed_test_history WHERE timestamp IN (:timestamps)")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteSpeedTestHistoryByTimestamps(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.Long> timestamps, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM data_usage WHERE date IN (:dates)")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteDataUsageByDates(@org.jetbrains.annotations.NotNull
    java.util.List<java.lang.String> dates, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE data_usage SET mobileData = mobileData + :mobile, wifiData = wifiData + :wifi, totalData = totalData + :mobile + :wifi WHERE date = :date")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object incrementUsage(@org.jetbrains.annotations.NotNull
    java.lang.String date, long mobile, long wifi, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insertEmptyUsage(@org.jetbrains.annotations.NotNull
    com.netspeedmeter.database.entity.DataUsageEntity entity, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM data_usage")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object clearAllDataUsage(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM speed_test_history")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object clearAllSpeedTestHistory(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}