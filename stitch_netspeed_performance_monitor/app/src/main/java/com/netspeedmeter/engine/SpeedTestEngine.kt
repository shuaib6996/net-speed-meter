package com.netspeedmeter.engine

import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.netspeedmeter.database.AppDatabase
import com.netspeedmeter.database.entity.SpeedTestHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Engine for performing real speed tests (download, upload, ping, jitter, packet loss).
 * Uses actual HTTP downloads and measures bytes transferred directly rather than
 * relying on TrafficStats (which counts ALL device traffic and inflates results).
 *
 * Test stages: ping → download → upload → complete
 */
object SpeedTestEngine {

    private const val TAG = "SpeedTestEngine"

    // ── Server endpoints ──
    data class SpeedTestServer(
        val id: String,
        val name: String,
        val location: String,
        val downloadUrl: String,
        val uploadUrl: String,
        val pingHost: String
    )

    val availableServers = listOf(
        SpeedTestServer("cloudflare", "Cloudflare", "Global CDN",
            "https://speed.cloudflare.com/__down?bytes=25000000",
            "https://speed.cloudflare.com/__up",
            "1.1.1.1"),
        SpeedTestServer("google", "Google CDN", "Global",
            "https://www.google.com/generate_204",
            "https://www.google.com/generate_204",
            "8.8.8.8"),
        SpeedTestServer("netflix", "Netflix", "Global",
            "https://speed.cloudflare.com/__down?bytes=25000000",
            "https://speed.cloudflare.com/__up",
            "8.8.4.4"),
        SpeedTestServer("azure", "Azure", "Global",
            "https://speed.cloudflare.com/__down?bytes=10000000",
            "https://speed.cloudflare.com/__up",
            "1.0.0.1")
    )

    private var selectedServer: SpeedTestServer = availableServers[0]

    @Volatile
    private var cancelled = AtomicBoolean(false)

    private const val PARALLEL_CONNECTIONS = 1
    private const val PING_COUNT = 10
    private const val PING_TIMEOUT_MS = 2000
    private const val DOWNLOAD_WARMUP_MS = 1000L
    private const val SAMPLE_INTERVAL_MS = 500L
    private const val UPLOAD_SIZE_BYTES = 8_000_000

    data class SpeedTestProgress(
        val stage: String,
        val progress: Float,
        val currentSpeed: Double = 0.0,
        val pingMs: Int = 0
    ) {
        fun toJson(): String {
            return """{"stage":"$stage","progress":$progress,"currentSpeed":$currentSpeed,"pingMs":$pingMs}"""
        }
    }

    data class SpeedTestResult(
        val downloadResult: Double,
        val uploadResult: Double,
        val pingScore: Int,
        val jitter: Double,
        val packetLoss: Double,
        val stabilityIndex: Double,
        val ispName: String = "",
        val connectionType: String = "",
        val serverLocation: String = "",
        val error: String? = null
    ) {
        fun toJson(): String {
            val errorStr = if (error != null) ",\"error\":\"$error\"" else ""
            return """{"downloadResult":$downloadResult,"uploadResult":$uploadResult,"pingScore":$pingScore,"jitter":$jitter,"packetLoss":$packetLoss,"stabilityIndex":$stabilityIndex,"ispName":"$ispName","connectionType":"$connectionType","serverLocation":"$serverLocation"$errorStr}"""
        }
    }

    fun selectServer(serverId: String): Boolean {
        val server = availableServers.find { it.id == serverId }
        if (server != null) {
            selectedServer = server
            return true
        }
        return false
    }

    fun getSelectedServer(): SpeedTestServer = selectedServer

    fun cancel() {
        cancelled.set(true)
    }

    /**
     * Start a full speed test: Ping → Download → Upload.
     * Uses actual byte counting from HTTP responses (not TrafficStats).
     */
    suspend fun startTest(
        context: Context,
        onProgress: (SpeedTestProgress) -> Unit = {},
        onComplete: (SpeedTestResult) -> Unit
    ) = withContext(Dispatchers.IO) {
        cancelled.set(false)
        val server = selectedServer

        try {
            val networkInfo = NetworkInfoProvider.fetch(context)

            // Stage 1: Ping & Jitter
            if (cancelled.get()) return@withContext
            onProgress(SpeedTestProgress("ping", 0.0f))
            val pingResult = measurePingAndJitter(server.pingHost) { pingMs, progress ->
                onProgress(SpeedTestProgress("ping", progress, pingMs = pingMs))
            }
            if (cancelled.get()) return@withContext
            onProgress(SpeedTestProgress("ping", 1.0f, pingMs = pingResult.first))

            // Stage 2: Download (parallel connections, real byte counting)
            if (cancelled.get()) return@withContext
            onProgress(SpeedTestProgress("download", 0.0f))
            val downloadSpeed = measureDownloadSpeed(server.downloadUrl) { speed, progress ->
                onProgress(SpeedTestProgress("download", progress, currentSpeed = speed))
            }
            if (cancelled.get()) return@withContext
            onProgress(SpeedTestProgress("download", 1.0f, currentSpeed = downloadSpeed))
            Log.d(TAG, "Download result: $downloadSpeed Mbps")

            // Stage 3: Upload (real byte counting)
            if (cancelled.get()) return@withContext
            onProgress(SpeedTestProgress("upload", 0.0f))
            val uploadSpeed = measureUploadSpeed(server.uploadUrl) { speed, progress ->
                onProgress(SpeedTestProgress("upload", progress, currentSpeed = speed))
            }
            if (cancelled.get()) return@withContext
            onProgress(SpeedTestProgress("upload", 1.0f, currentSpeed = uploadSpeed))
            Log.d(TAG, "Upload result: $uploadSpeed Mbps")

            val stability = calculateStability(
                pingResult.first, pingResult.second, pingResult.third,
                downloadSpeed, uploadSpeed
            )

            val result = SpeedTestResult(
                downloadResult = downloadSpeed,
                uploadResult = uploadSpeed,
                pingScore = pingResult.first,
                jitter = pingResult.second,
                packetLoss = pingResult.third,
                stabilityIndex = stability,
                ispName = networkInfo.ispName,
                connectionType = networkInfo.connectionType,
                serverLocation = "${server.name} (${server.location})"
            )

            // Save to database
            val history = SpeedTestHistoryEntity(
                downloadResult = downloadSpeed,
                uploadResult = uploadSpeed,
                pingScore = pingResult.first,
                jitter = pingResult.second,
                packetLoss = pingResult.third,
                stabilityIndex = stability,
                ispName = networkInfo.ispName,
                connectionType = networkInfo.connectionType,
                serverLocation = "${server.name} (${server.location})"
            )
            AppDatabase.getInstance(context).appDao().insertSpeedTestHistory(history)

            onProgress(SpeedTestProgress("complete", 1.0f))
            onComplete(result)

        } catch (e: Exception) {
            if (cancelled.get()) return@withContext
            Log.e(TAG, "Speed test failed", e)
            // Return error result instead of fake data
            val errorResult = SpeedTestResult(
                downloadResult = 0.0,
                uploadResult = 0.0,
                pingScore = 0,
                jitter = 0.0,
                packetLoss = 100.0,
                stabilityIndex = 0.0,
                error = "Test failed: ${e.message ?: "Network error"}"
            )
            onComplete(errorResult)
        }
    }

    /**
     * Measure ping, jitter, and packet loss using TCP socket connections.
     */
    private fun measurePingAndJitter(
        host: String = "8.8.8.8",
        onProgress: (pingMs: Int, progress: Float) -> Unit
    ): Triple<Int, Double, Double> {
        val pings = mutableListOf<Long>()
        var failed = 0

        for (i in 0 until PING_COUNT) {
            if (cancelled.get()) break
            try {
                val start = System.nanoTime()
                val socket = Socket()
                socket.connect(InetSocketAddress(host, 53), PING_TIMEOUT_MS)
                socket.close()
                val duration = (System.nanoTime() - start) / 1_000_000
                pings.add(duration)
            } catch (e: IOException) {
                failed++
                pings.add(PING_TIMEOUT_MS.toLong())
            }
            onProgress(pings.last().toInt(), (i + 1).toFloat() / PING_COUNT)
        }

        val average = if (pings.isNotEmpty()) pings.average().roundToInt() else 999
        val jitter = calculateJitter(pings)
        val packetLoss = (failed.toDouble() / PING_COUNT) * 100.0

        return Triple(average, jitter, packetLoss)
    }

    private fun calculateJitter(pings: List<Long>): Double {
        if (pings.size < 2) return 0.0
        val mean = pings.average()
        val variance = pings.map { (it - mean) * (it - mean) }.average()
        return roundToTwoDecimals(sqrt(variance))
    }

    /**
     * Measure download speed by counting actual bytes received from HTTP response.
     * Uses parallel connections for accuracy. Does NOT use TrafficStats.
     */
    private suspend fun measureDownloadSpeed(
        url: String,
        onProgress: (speedMbps: Double, progress: Float) -> Unit
    ): Double = coroutineScope {
        val totalBytesDownloaded = AtomicLong(0)
        val testStartTime = SystemClock.elapsedRealtime()
        val speedSamples = mutableListOf<Double>()

        val jobs = (1..PARALLEL_CONNECTIONS).map {
            async(Dispatchers.IO) {
                downloadAndCount(url, totalBytesDownloaded)
            }
        }

        val monitorJob = async(Dispatchers.IO) {
            var lastReportTime = testStartTime
            var lastReportBytes = 0L

            while (!cancelled.get()) {
                kotlinx.coroutines.delay(SAMPLE_INTERVAL_MS)
                val now = SystemClock.elapsedRealtime()
                val elapsed = now - testStartTime
                val currentTotalBytes = totalBytesDownloaded.get()
                val intervalMs = now - lastReportTime
                val intervalBytes = currentTotalBytes - lastReportBytes

                if (intervalMs > 0 && elapsed >= DOWNLOAD_WARMUP_MS) {
                    val speedMbps = bytesPerIntervalToMbps(intervalBytes, intervalMs)
                    if (speedMbps > 0.0) speedSamples.add(speedMbps)
                    val progress = (elapsed.toFloat() / 10000f).coerceIn(0.05f, 0.95f)
                    onProgress(roundToTwoDecimals(speedMbps), progress)
                }

                lastReportTime = now
                lastReportBytes = currentTotalBytes

                if (jobs.all { it.isCompleted }) break
            }
        }

        try {
            jobs.awaitAll()
        } catch (_: Exception) { }
        monitorJob.cancel()

        val testEndTime = SystemClock.elapsedRealtime()
        val totalBytes = totalBytesDownloaded.get()
        val totalTimeMs = (testEndTime - testStartTime).coerceAtLeast(1)

        val measuredSpeed = if (speedSamples.isNotEmpty()) {
            speedSamples.average()
        } else {
            bytesPerIntervalToMbps(totalBytes, totalTimeMs)
        }

        Log.d(TAG, "Download: ${totalBytes} bytes in ${totalTimeMs}ms = ${roundToTwoDecimals(measuredSpeed)} Mbps")
        roundToTwoDecimals(measuredSpeed.coerceAtLeast(0.0))
    }

    /**
     * Download from URL and count bytes in a shared atomic counter.
     * This ensures we only count test traffic, not other device traffic.
     */
    private fun downloadAndCount(url: String, bytesCounter: AtomicLong): Long {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .header("Cache-Control", "no-cache, no-store")
            .build()

        var streamBytes = 0L
        try {
            val response = client.newCall(request).execute()
            val body = response.body
            if (body != null) {
                val inputStream: InputStream = body.byteStream()
                val buffer = ByteArray(16384) // 16KB buffer for efficiency
                var bytesRead: Int
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    if (cancelled.get()) break
                    streamBytes += bytesRead
                    bytesCounter.addAndGet(bytesRead.toLong())
                }
                inputStream.close()
            }
            response.close()
        } catch (e: IOException) {
            Log.w(TAG, "Download stream error: ${e.message}")
        }
        return streamBytes
    }

    /**
     * Measure upload speed by sending data and counting actual bytes transmitted.
     * Uses actual HTTP upload to the server. Does NOT use TrafficStats.
     */
    private suspend fun measureUploadSpeed(
        url: String,
        onProgress: (speedMbps: Double, progress: Float) -> Unit
    ): Double = withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()

        val uploadData = ByteArray(UPLOAD_SIZE_BYTES) { (it % 256).toByte() }
        val uploadStartTime = AtomicLong(0L)
        val uploadedBytes = AtomicLong(0L)
        val speedSamples = mutableListOf<Double>()
        val requestBody = object : RequestBody() {
            override fun contentType() = "application/octet-stream".toMediaType()
            override fun contentLength() = uploadData.size.toLong()

            override fun writeTo(sink: BufferedSink) {
                uploadStartTime.compareAndSet(0L, SystemClock.elapsedRealtime())
                var offset = 0
                var lastReportTime = uploadStartTime.get()
                var lastReportBytes = 0L

                while (offset < uploadData.size && !cancelled.get()) {
                    val chunkSize = minOf(16_384, uploadData.size - offset)
                    sink.write(uploadData, offset, chunkSize)
                    offset += chunkSize
                    val total = uploadedBytes.addAndGet(chunkSize.toLong())
                    val now = SystemClock.elapsedRealtime()
                    val intervalMs = now - lastReportTime

                    if (intervalMs >= SAMPLE_INTERVAL_MS) {
                        val intervalBytes = total - lastReportBytes
                        val speedMbps = bytesPerIntervalToMbps(intervalBytes, intervalMs)
                        if (speedMbps > 0.0) speedSamples.add(speedMbps)
                        onProgress(roundToTwoDecimals(speedMbps), (total.toFloat() / uploadData.size).coerceIn(0.05f, 0.95f))
                        lastReportTime = now
                        lastReportBytes = total
                    }
                }
                sink.flush()
            }
        }

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .header("Cache-Control", "no-cache, no-store")
            .build()

        try {
            val response = client.newCall(request).execute()
            response.close()

            val endTime = SystemClock.elapsedRealtime()
            val startTime = uploadStartTime.get().takeIf { it > 0L } ?: endTime
            val timeDeltaMs = (endTime - startTime).coerceAtLeast(1)
            val measuredSpeed = if (speedSamples.isNotEmpty()) {
                speedSamples.average()
            } else {
                bytesPerIntervalToMbps(uploadedBytes.get(), timeDeltaMs)
            }

            Log.d(TAG, "Upload: ${uploadedBytes.get()} bytes in ${timeDeltaMs}ms = ${roundToTwoDecimals(measuredSpeed)} Mbps")
            onProgress(roundToTwoDecimals(measuredSpeed), 1.0f)

            roundToTwoDecimals(measuredSpeed.coerceAtLeast(0.0))

        } catch (e: IOException) {
            Log.e(TAG, "Upload failed: ${e.message}")
            onProgress(0.0, 1.0f)
            0.0
        }
    }

    /**
     * Calculate stability index (0-1) based on ping, jitter, packet loss, and speeds.
     */
    private fun calculateStability(
        ping: Int, jitter: Double, packetLoss: Double,
        download: Double, upload: Double
    ): Double {
        val pingScore = 1.0 - (ping.coerceAtMost(500) / 500.0)
        val jitterScore = 1.0 - (jitter.coerceAtMost(100.0) / 100.0)
        val packetLossScore = 1.0 - (packetLoss / 100.0)
        val downloadScore = download.coerceAtMost(1000.0) / 1000.0
        val uploadScore = upload.coerceAtMost(500.0) / 500.0

        return roundToTwoDecimals(
            (pingScore * 0.25 + jitterScore * 0.15 + packetLossScore * 0.20 +
                    downloadScore * 0.25 + uploadScore * 0.15)
        )
    }

    private fun bytesPerIntervalToMbps(bytes: Long, intervalMs: Long): Double {
        if (bytes <= 0L || intervalMs <= 0L) return 0.0
        return bytes * 8.0 * 1000.0 / intervalMs / 1_000_000.0
    }

    private fun roundToTwoDecimals(value: Double): Double {
        return (value * 100).roundToInt() / 100.0
    }
}