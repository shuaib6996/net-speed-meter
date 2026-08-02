package com.netspeedmeter.engine

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.TelephonyManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Provides network information: ISP name, connection type, external IP,
 * and approximate server location. Used to enrich speed test results.
 */
object NetworkInfoProvider {

    data class NetworkInfo(
        val ispName: String,
        val connectionType: String,
        val externalIp: String,
        val serverLocation: String,
        val networkTypeIcon: String // material icon name
    ) {
        fun toJson(): String {
            return """{"ispName":"$ispName","connectionType":"$connectionType","externalIp":"$externalIp","serverLocation":"$serverLocation","networkTypeIcon":"$networkTypeIcon"}"""
        }
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    /**
     * Fetch network info. ISP and connection type come from device APIs;
     * external IP and location come from public web APIs.
     */
    suspend fun fetch(context: Context): NetworkInfo = withContext(Dispatchers.IO) {
        val connectionType = detectConnectionType(context)
        val ispName = detectIspName(context)
        val icon = getNetworkIcon(connectionType)

        // Fetch external IP and location from public APIs
        var externalIp = "Unknown"
        var serverLocation = "Unknown"

        try {
            val ipRequest = Request.Builder()
                .url("https://api.ipify.org?format=json")
                .build()
            val ipResponse = client.newCall(ipRequest).execute()
            val ipBody = ipResponse.body?.string()
            if (ipBody != null) {
                externalIp = JSONObject(ipBody).optString("ip", "Unknown")
            }
        } catch (_: Exception) {
            // IP lookup failed, use fallback
        }

        try {
            val locRequest = Request.Builder()
                .url("https://ipapi.co/$externalIp/json/")
                .build()
            val locResponse = client.newCall(locRequest).execute()
            val locBody = locResponse.body?.string()
            if (locBody != null) {
                val json = JSONObject(locBody)
                val city = json.optString("city", "")
                val country = json.optString("country_name", "")
                serverLocation = if (city.isNotEmpty() && country.isNotEmpty()) {
                    "$city, $country"
                } else if (country.isNotEmpty()) {
                    country
                } else {
                    "Unknown"
                }
                // Use ISP from ipapi if available and device ISP is unknown
                if (ispName == "Unknown") {
                    val org = json.optString("org", "")
                    if (org.isNotEmpty()) {
                        return@withContext NetworkInfo(
                            ispName = org,
                            connectionType = connectionType,
                            externalIp = externalIp,
                            serverLocation = serverLocation,
                            networkTypeIcon = icon
                        )
                    }
                }
            }
        } catch (_: Exception) {
            // Location lookup failed
        }

        NetworkInfo(
            ispName = ispName,
            connectionType = connectionType,
            externalIp = externalIp,
            serverLocation = serverLocation,
            networkTypeIcon = icon
        )
    }

    /**
     * Detect connection type: WiFi, 5G, 4G, 3G, 2G, Ethernet, or Unknown.
     */
    fun detectConnectionType(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return "Unknown"

        val network = cm.activeNetwork ?: return "Unknown"
        val caps = cm.getNetworkCapabilities(network) ?: return "Unknown"

        return when {
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                detectCellularGeneration(context)
            }
            caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
            else -> "Unknown"
        }
    }

    /**
     * Detect cellular network generation: 5G, 4G/LTE, 3G, 2G.
     * Wrapped in try-catch because dataNetworkType requires READ_PHONE_STATE
     * permission which we don't declare in the manifest.
     */
    private fun detectCellularGeneration(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            ?: return "Mobile"

        val networkType = try {
            tm.dataNetworkType
        } catch (_: SecurityException) {
            // READ_PHONE_STATE permission not granted; fall back to ConnectivityManager
            return detectCellularGenerationFallback(context)
        }

        return when (networkType) {
            TelephonyManager.NETWORK_TYPE_NR -> "5G"
            TelephonyManager.NETWORK_TYPE_LTE -> "4G LTE"
            TelephonyManager.NETWORK_TYPE_HSPAP,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA -> "3G HSPA+"
            TelephonyManager.NETWORK_TYPE_UMTS -> "3G UMTS"
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD -> "3G EVDO"
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_GPRS -> "2G"
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT -> "2G CDMA"
            TelephonyManager.NETWORK_TYPE_IDEN -> "2G iDEN"
            else -> "Mobile"
        }
    }

    /**
     * Fallback cellular detection using ConnectivityManager when
     * TelephonyManager.dataNetworkType is inaccessible.
     */
    private fun detectCellularGenerationFallback(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return "Mobile"
        val network = cm.activeNetwork ?: return "Mobile"
        val caps = cm.getNetworkCapabilities(network) ?: return "Mobile"

        // On Android 12+, check for 5G capability
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) &&
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "4G LTE"
                else -> "Mobile"
            }
        } else {
            "Mobile"
        }
    }

    /**
     * Attempt to detect ISP name from SIM carrier or network operator.
     */
    private fun detectIspName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
            ?: return "Unknown"

        // Try SIM operator first, then network operator
        val simOperator = tm.simOperatorName
        if (!simOperator.isNullOrEmpty()) return simOperator

        val networkOperator = tm.networkOperatorName
        if (!networkOperator.isNullOrEmpty()) return networkOperator

        return "Unknown"
    }

    /**
     * Get Material icon name for the connection type.
     */
    fun getNetworkIcon(connectionType: String): String {
        return when {
            connectionType.contains("WiFi", ignoreCase = true) -> "wifi"
            connectionType.contains("5G", ignoreCase = true) -> "5g"
            connectionType.contains("4G", ignoreCase = true) -> "4g_mobiledata"
            connectionType.contains("3G", ignoreCase = true) -> "3g_mobiledata"
            connectionType.contains("2G", ignoreCase = true) -> "signal_cellular_alt"
            connectionType.contains("Ethernet", ignoreCase = true) -> "lan"
            connectionType.contains("VPN", ignoreCase = true) -> "vpn_key"
            else -> "signal_cellular_alt"
        }
    }
}