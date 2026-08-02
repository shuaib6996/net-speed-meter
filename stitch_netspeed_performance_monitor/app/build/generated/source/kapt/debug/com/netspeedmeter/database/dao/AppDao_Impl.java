package com.netspeedmeter.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.netspeedmeter.database.entity.DataUsageEntity;
import com.netspeedmeter.database.entity.SpeedLogEntity;
import com.netspeedmeter.database.entity.SpeedTestHistoryEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDao_Impl implements AppDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SpeedLogEntity> __insertionAdapterOfSpeedLogEntity;

  private final EntityInsertionAdapter<DataUsageEntity> __insertionAdapterOfDataUsageEntity;

  private final EntityInsertionAdapter<SpeedTestHistoryEntity> __insertionAdapterOfSpeedTestHistoryEntity;

  private final EntityInsertionAdapter<DataUsageEntity> __insertionAdapterOfDataUsageEntity_1;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldSpeedLogs;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldSpeedTestHistory;

  private final SharedSQLiteStatement __preparedStmtOfIncrementUsage;

  private final SharedSQLiteStatement __preparedStmtOfClearAllDataUsage;

  private final SharedSQLiteStatement __preparedStmtOfClearAllSpeedTestHistory;

  public AppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSpeedLogEntity = new EntityInsertionAdapter<SpeedLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `speed_log` (`id`,`timestamp`,`downloadSpeed`,`uploadSpeed`,`ping`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SpeedLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindDouble(3, entity.getDownloadSpeed());
        statement.bindDouble(4, entity.getUploadSpeed());
        statement.bindLong(5, entity.getPing());
      }
    };
    this.__insertionAdapterOfDataUsageEntity = new EntityInsertionAdapter<DataUsageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `data_usage` (`date`,`mobileData`,`wifiData`,`totalData`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DataUsageEntity entity) {
        if (entity.getDate() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDate());
        }
        statement.bindLong(2, entity.getMobileData());
        statement.bindLong(3, entity.getWifiData());
        statement.bindLong(4, entity.getTotalData());
      }
    };
    this.__insertionAdapterOfSpeedTestHistoryEntity = new EntityInsertionAdapter<SpeedTestHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `speed_test_history` (`id`,`timestamp`,`downloadResult`,`uploadResult`,`pingScore`,`jitter`,`packetLoss`,`stabilityIndex`,`ispName`,`connectionType`,`serverLocation`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SpeedTestHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindDouble(3, entity.getDownloadResult());
        statement.bindDouble(4, entity.getUploadResult());
        statement.bindLong(5, entity.getPingScore());
        statement.bindDouble(6, entity.getJitter());
        statement.bindDouble(7, entity.getPacketLoss());
        statement.bindDouble(8, entity.getStabilityIndex());
        if (entity.getIspName() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getIspName());
        }
        if (entity.getConnectionType() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getConnectionType());
        }
        if (entity.getServerLocation() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getServerLocation());
        }
      }
    };
    this.__insertionAdapterOfDataUsageEntity_1 = new EntityInsertionAdapter<DataUsageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `data_usage` (`date`,`mobileData`,`wifiData`,`totalData`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DataUsageEntity entity) {
        if (entity.getDate() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getDate());
        }
        statement.bindLong(2, entity.getMobileData());
        statement.bindLong(3, entity.getWifiData());
        statement.bindLong(4, entity.getTotalData());
      }
    };
    this.__preparedStmtOfDeleteOldSpeedLogs = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM speed_log WHERE timestamp < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldSpeedTestHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM speed_test_history WHERE timestamp < ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementUsage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE data_usage SET mobileData = mobileData + ?, wifiData = wifiData + ?, totalData = totalData + ? + ? WHERE date = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllDataUsage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM data_usage";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllSpeedTestHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM speed_test_history";
        return _query;
      }
    };
  }

  @Override
  public Object insertSpeedLog(final SpeedLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSpeedLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDataUsage(final DataUsageEntity usage,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDataUsageEntity.insert(usage);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSpeedTestHistory(final SpeedTestHistoryEntity history,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSpeedTestHistoryEntity.insert(history);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertEmptyUsage(final DataUsageEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDataUsageEntity_1.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldSpeedLogs(final long olderThan,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldSpeedLogs.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, olderThan);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldSpeedLogs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldSpeedTestHistory(final long olderThan,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldSpeedTestHistory.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, olderThan);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldSpeedTestHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementUsage(final String date, final long mobile, final long wifi,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementUsage.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, mobile);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, wifi);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, mobile);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, wifi);
        _argIndex = 5;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, date);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementUsage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllDataUsage(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllDataUsage.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllDataUsage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllSpeedTestHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllSpeedTestHistory.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllSpeedTestHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SpeedLogEntity>> getSpeedLogs(final int limit) {
    final String _sql = "SELECT * FROM speed_log ORDER BY timestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"speed_log"}, new Callable<List<SpeedLogEntity>>() {
      @Override
      @NonNull
      public List<SpeedLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDownloadSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadSpeed");
          final int _cursorIndexOfUploadSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadSpeed");
          final int _cursorIndexOfPing = CursorUtil.getColumnIndexOrThrow(_cursor, "ping");
          final List<SpeedLogEntity> _result = new ArrayList<SpeedLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SpeedLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final double _tmpDownloadSpeed;
            _tmpDownloadSpeed = _cursor.getDouble(_cursorIndexOfDownloadSpeed);
            final double _tmpUploadSpeed;
            _tmpUploadSpeed = _cursor.getDouble(_cursorIndexOfUploadSpeed);
            final int _tmpPing;
            _tmpPing = _cursor.getInt(_cursorIndexOfPing);
            _item = new SpeedLogEntity(_tmpId,_tmpTimestamp,_tmpDownloadSpeed,_tmpUploadSpeed,_tmpPing);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getDataUsage(final String date,
      final Continuation<? super DataUsageEntity> $completion) {
    final String _sql = "SELECT * FROM data_usage WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DataUsageEntity>() {
      @Override
      @Nullable
      public DataUsageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMobileData = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileData");
          final int _cursorIndexOfWifiData = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiData");
          final int _cursorIndexOfTotalData = CursorUtil.getColumnIndexOrThrow(_cursor, "totalData");
          final DataUsageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final long _tmpMobileData;
            _tmpMobileData = _cursor.getLong(_cursorIndexOfMobileData);
            final long _tmpWifiData;
            _tmpWifiData = _cursor.getLong(_cursorIndexOfWifiData);
            final long _tmpTotalData;
            _tmpTotalData = _cursor.getLong(_cursorIndexOfTotalData);
            _result = new DataUsageEntity(_tmpDate,_tmpMobileData,_tmpWifiData,_tmpTotalData);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DataUsageEntity>> getDataUsageHistory(final int limit) {
    final String _sql = "SELECT * FROM data_usage ORDER BY date DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"data_usage"}, new Callable<List<DataUsageEntity>>() {
      @Override
      @NonNull
      public List<DataUsageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMobileData = CursorUtil.getColumnIndexOrThrow(_cursor, "mobileData");
          final int _cursorIndexOfWifiData = CursorUtil.getColumnIndexOrThrow(_cursor, "wifiData");
          final int _cursorIndexOfTotalData = CursorUtil.getColumnIndexOrThrow(_cursor, "totalData");
          final List<DataUsageEntity> _result = new ArrayList<DataUsageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DataUsageEntity _item;
            final String _tmpDate;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmpDate = null;
            } else {
              _tmpDate = _cursor.getString(_cursorIndexOfDate);
            }
            final long _tmpMobileData;
            _tmpMobileData = _cursor.getLong(_cursorIndexOfMobileData);
            final long _tmpWifiData;
            _tmpWifiData = _cursor.getLong(_cursorIndexOfWifiData);
            final long _tmpTotalData;
            _tmpTotalData = _cursor.getLong(_cursorIndexOfTotalData);
            _item = new DataUsageEntity(_tmpDate,_tmpMobileData,_tmpWifiData,_tmpTotalData);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<SpeedTestHistoryEntity>> getSpeedTestHistory() {
    final String _sql = "SELECT * FROM speed_test_history ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"speed_test_history"}, new Callable<List<SpeedTestHistoryEntity>>() {
      @Override
      @NonNull
      public List<SpeedTestHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfDownloadResult = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadResult");
          final int _cursorIndexOfUploadResult = CursorUtil.getColumnIndexOrThrow(_cursor, "uploadResult");
          final int _cursorIndexOfPingScore = CursorUtil.getColumnIndexOrThrow(_cursor, "pingScore");
          final int _cursorIndexOfJitter = CursorUtil.getColumnIndexOrThrow(_cursor, "jitter");
          final int _cursorIndexOfPacketLoss = CursorUtil.getColumnIndexOrThrow(_cursor, "packetLoss");
          final int _cursorIndexOfStabilityIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "stabilityIndex");
          final int _cursorIndexOfIspName = CursorUtil.getColumnIndexOrThrow(_cursor, "ispName");
          final int _cursorIndexOfConnectionType = CursorUtil.getColumnIndexOrThrow(_cursor, "connectionType");
          final int _cursorIndexOfServerLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "serverLocation");
          final List<SpeedTestHistoryEntity> _result = new ArrayList<SpeedTestHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SpeedTestHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final double _tmpDownloadResult;
            _tmpDownloadResult = _cursor.getDouble(_cursorIndexOfDownloadResult);
            final double _tmpUploadResult;
            _tmpUploadResult = _cursor.getDouble(_cursorIndexOfUploadResult);
            final int _tmpPingScore;
            _tmpPingScore = _cursor.getInt(_cursorIndexOfPingScore);
            final double _tmpJitter;
            _tmpJitter = _cursor.getDouble(_cursorIndexOfJitter);
            final double _tmpPacketLoss;
            _tmpPacketLoss = _cursor.getDouble(_cursorIndexOfPacketLoss);
            final double _tmpStabilityIndex;
            _tmpStabilityIndex = _cursor.getDouble(_cursorIndexOfStabilityIndex);
            final String _tmpIspName;
            if (_cursor.isNull(_cursorIndexOfIspName)) {
              _tmpIspName = null;
            } else {
              _tmpIspName = _cursor.getString(_cursorIndexOfIspName);
            }
            final String _tmpConnectionType;
            if (_cursor.isNull(_cursorIndexOfConnectionType)) {
              _tmpConnectionType = null;
            } else {
              _tmpConnectionType = _cursor.getString(_cursorIndexOfConnectionType);
            }
            final String _tmpServerLocation;
            if (_cursor.isNull(_cursorIndexOfServerLocation)) {
              _tmpServerLocation = null;
            } else {
              _tmpServerLocation = _cursor.getString(_cursorIndexOfServerLocation);
            }
            _item = new SpeedTestHistoryEntity(_tmpId,_tmpTimestamp,_tmpDownloadResult,_tmpUploadResult,_tmpPingScore,_tmpJitter,_tmpPacketLoss,_tmpStabilityIndex,_tmpIspName,_tmpConnectionType,_tmpServerLocation);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object deleteSpeedTestHistoryByTimestamps(final List<Long> timestamps,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM speed_test_history WHERE timestamp IN (");
        final int _inputSize = timestamps.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (Long _item : timestamps) {
          if (_item == null) {
            _stmt.bindNull(_argIndex);
          } else {
            _stmt.bindLong(_argIndex, _item);
          }
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDataUsageByDates(final List<String> dates,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM data_usage WHERE date IN (");
        final int _inputSize = dates.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (String _item : dates) {
          if (_item == null) {
            _stmt.bindNull(_argIndex);
          } else {
            _stmt.bindString(_argIndex, _item);
          }
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
