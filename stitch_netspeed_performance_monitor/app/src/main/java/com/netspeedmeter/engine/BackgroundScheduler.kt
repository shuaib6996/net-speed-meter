package com.netspeedmeter.engine

import android.content.Context
import androidx.work.*
import com.netspeedmeter.database.AppDatabase
import com.netspeedmeter.service.NetworkMonitorService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Schedules periodic background tasks:
 * - Every 1 second: speed updates (handled by Foreground Service)
 * - Every 5 minutes: save speed log to database
 * - Every 24 hours: update data usage summary
 */
object BackgroundScheduler {

    private const val SPEED_LOG_WORKER_TAG = "speed_log_worker"
    private const val DATA_USAGE_WORKER_TAG = "data_usage_worker"
    private const val CLEANUP_WORKER_TAG = "cleanup_worker"

    fun scheduleAll(context: Context) {
        scheduleSpeedLogWorker(context)
        scheduleDataUsageWorker(context)
        scheduleCleanupWorker(context)
    }

    private fun scheduleSpeedLogWorker(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            // Removed battery constraint to allow logging even with low battery
            .build()

        val request = PeriodicWorkRequest.Builder(
            SpeedLogWorker::class.java,
            15, TimeUnit.MINUTES, // WorkManager minimum interval
            5, TimeUnit.MINUTES
        ).setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
            .addTag(SPEED_LOG_WORKER_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SPEED_LOG_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun scheduleDataUsageWorker(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequest.Builder(
            DataUsageWorker::class.java,
            24, TimeUnit.HOURS, // every 24 hours
            1, TimeUnit.HOURS
        ).setConstraints(constraints)
            .addTag(DATA_USAGE_WORKER_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DATA_USAGE_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun scheduleCleanupWorker(context: Context) {
        val request = PeriodicWorkRequest.Builder(
            CleanupWorker::class.java,
            7, TimeUnit.DAYS, // weekly cleanup
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            CLEANUP_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    class SpeedLogWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
            try {
                val speed = NetworkMonitorService.speedLiveData.value
                if (speed != null) {
                    val dao = AppDatabase.getInstance(applicationContext).appDao()
                    dao.insertSpeedLog(
                        com.netspeedmeter.database.entity.SpeedLogEntity(
                            downloadSpeed = speed.downloadSpeed,
                            uploadSpeed = speed.uploadSpeed,
                            ping = speed.ping
                        )
                    )
                    Result.success()
                } else {
                    if (SettingsManager.isServiceEnabled(applicationContext)) {
                        androidx.core.content.ContextCompat.startForegroundService(
                            applicationContext,
                            android.content.Intent(applicationContext, NetworkMonitorService::class.java)
                        )
                    }
                    Result.success()
                }
            } catch (e: Exception) {
                // Log error and retry with backoff
                Result.retry()
            }
        }
    }

    class DataUsageWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
            try {
                DataUsageTracker.updateUsageSnapshot(applicationContext)
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
        }
    }

    class CleanupWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
        override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
            try {
                val dao = AppDatabase.getInstance(applicationContext).appDao()
                val thirtyDaysAgo = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
                dao.deleteOldSpeedLogs(thirtyDaysAgo)
                dao.deleteOldSpeedTestHistory(thirtyDaysAgo)
                Result.success()
            } catch (e: Exception) {
                // Log cleanup error and retry
                Result.retry()
            }
        }
    }
}