package com.netspeedmeter.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.netspeedmeter.database.dao.AppDao;
import com.netspeedmeter.database.dao.AppDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AppDao _appDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `speed_log` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `downloadSpeed` REAL NOT NULL, `uploadSpeed` REAL NOT NULL, `ping` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `idx_speed_log_timestamp` ON `speed_log` (`timestamp`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `data_usage` (`date` TEXT NOT NULL, `mobileData` INTEGER NOT NULL, `wifiData` INTEGER NOT NULL, `totalData` INTEGER NOT NULL, PRIMARY KEY(`date`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `idx_data_usage_date` ON `data_usage` (`date`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `speed_test_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `downloadResult` REAL NOT NULL, `uploadResult` REAL NOT NULL, `pingScore` INTEGER NOT NULL, `jitter` REAL NOT NULL, `packetLoss` REAL NOT NULL, `stabilityIndex` REAL NOT NULL, `ispName` TEXT NOT NULL, `connectionType` TEXT NOT NULL, `serverLocation` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f349491f116350149f77f1a769faa6ba')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `speed_log`");
        db.execSQL("DROP TABLE IF EXISTS `data_usage`");
        db.execSQL("DROP TABLE IF EXISTS `speed_test_history`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSpeedLog = new HashMap<String, TableInfo.Column>(5);
        _columnsSpeedLog.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedLog.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedLog.put("downloadSpeed", new TableInfo.Column("downloadSpeed", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedLog.put("uploadSpeed", new TableInfo.Column("uploadSpeed", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedLog.put("ping", new TableInfo.Column("ping", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpeedLog = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpeedLog = new HashSet<TableInfo.Index>(1);
        _indicesSpeedLog.add(new TableInfo.Index("idx_speed_log_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        final TableInfo _infoSpeedLog = new TableInfo("speed_log", _columnsSpeedLog, _foreignKeysSpeedLog, _indicesSpeedLog);
        final TableInfo _existingSpeedLog = TableInfo.read(db, "speed_log");
        if (!_infoSpeedLog.equals(_existingSpeedLog)) {
          return new RoomOpenHelper.ValidationResult(false, "speed_log(com.netspeedmeter.database.entity.SpeedLogEntity).\n"
                  + " Expected:\n" + _infoSpeedLog + "\n"
                  + " Found:\n" + _existingSpeedLog);
        }
        final HashMap<String, TableInfo.Column> _columnsDataUsage = new HashMap<String, TableInfo.Column>(4);
        _columnsDataUsage.put("date", new TableInfo.Column("date", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataUsage.put("mobileData", new TableInfo.Column("mobileData", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataUsage.put("wifiData", new TableInfo.Column("wifiData", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDataUsage.put("totalData", new TableInfo.Column("totalData", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDataUsage = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDataUsage = new HashSet<TableInfo.Index>(1);
        _indicesDataUsage.add(new TableInfo.Index("idx_data_usage_date", true, Arrays.asList("date"), Arrays.asList("ASC")));
        final TableInfo _infoDataUsage = new TableInfo("data_usage", _columnsDataUsage, _foreignKeysDataUsage, _indicesDataUsage);
        final TableInfo _existingDataUsage = TableInfo.read(db, "data_usage");
        if (!_infoDataUsage.equals(_existingDataUsage)) {
          return new RoomOpenHelper.ValidationResult(false, "data_usage(com.netspeedmeter.database.entity.DataUsageEntity).\n"
                  + " Expected:\n" + _infoDataUsage + "\n"
                  + " Found:\n" + _existingDataUsage);
        }
        final HashMap<String, TableInfo.Column> _columnsSpeedTestHistory = new HashMap<String, TableInfo.Column>(11);
        _columnsSpeedTestHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("downloadResult", new TableInfo.Column("downloadResult", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("uploadResult", new TableInfo.Column("uploadResult", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("pingScore", new TableInfo.Column("pingScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("jitter", new TableInfo.Column("jitter", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("packetLoss", new TableInfo.Column("packetLoss", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("stabilityIndex", new TableInfo.Column("stabilityIndex", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("ispName", new TableInfo.Column("ispName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("connectionType", new TableInfo.Column("connectionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSpeedTestHistory.put("serverLocation", new TableInfo.Column("serverLocation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSpeedTestHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSpeedTestHistory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSpeedTestHistory = new TableInfo("speed_test_history", _columnsSpeedTestHistory, _foreignKeysSpeedTestHistory, _indicesSpeedTestHistory);
        final TableInfo _existingSpeedTestHistory = TableInfo.read(db, "speed_test_history");
        if (!_infoSpeedTestHistory.equals(_existingSpeedTestHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "speed_test_history(com.netspeedmeter.database.entity.SpeedTestHistoryEntity).\n"
                  + " Expected:\n" + _infoSpeedTestHistory + "\n"
                  + " Found:\n" + _existingSpeedTestHistory);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f349491f116350149f77f1a769faa6ba", "1284c571871b14e7d8c59befe5a0cfab");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "speed_log","data_usage","speed_test_history");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `speed_log`");
      _db.execSQL("DELETE FROM `data_usage`");
      _db.execSQL("DELETE FROM `speed_test_history`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AppDao.class, AppDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AppDao appDao() {
    if (_appDao != null) {
      return _appDao;
    } else {
      synchronized(this) {
        if(_appDao == null) {
          _appDao = new AppDao_Impl(this);
        }
        return _appDao;
      }
    }
  }
}
