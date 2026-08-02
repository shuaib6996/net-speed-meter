package com.netspeedmeter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.netspeedmeter.database.dao.AppDao
import com.netspeedmeter.database.entity.DataUsageEntity
import com.netspeedmeter.database.entity.SpeedLogEntity
import com.netspeedmeter.database.entity.SpeedTestHistoryEntity

@Database(
    entities = [
        SpeedLogEntity::class,
        DataUsageEntity::class,
        SpeedTestHistoryEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Migration from version 1 to 2:
         * Adds jitter, packetLoss, ispName, connectionType, serverLocation columns
         * to speed_test_history table.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    ALTER TABLE speed_test_history 
                    ADD COLUMN jitter REAL NOT NULL DEFAULT 0.0
                """)
                database.execSQL("""
                    ALTER TABLE speed_test_history 
                    ADD COLUMN packetLoss REAL NOT NULL DEFAULT 0.0
                """)
                database.execSQL("""
                    ALTER TABLE speed_test_history 
                    ADD COLUMN ispName TEXT NOT NULL DEFAULT ''
                """)
                database.execSQL("""
                    ALTER TABLE speed_test_history 
                    ADD COLUMN connectionType TEXT NOT NULL DEFAULT ''
                """)
                database.execSQL("""
                    ALTER TABLE speed_test_history 
                    ADD COLUMN serverLocation TEXT NOT NULL DEFAULT ''
                """)
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "netspeed_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}