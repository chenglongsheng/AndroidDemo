package com.buyehou.toolbox.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import com.buyehou.toolbox.Toolbox

/**
 * @author Rosen
 * @date 2023/10/7 12:20
 */
object NetworkUtils {

    private val connectivityManager: ConnectivityManager?
        get() = ContextCompat.getSystemService(
            Toolbox.instance.globalContext, ConnectivityManager::class.java
        )

    private fun getCurrentNetWork(): Network? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager?.activeNetwork
        } else {
            connectivityManager?.allNetworks?.takeIf {
                it.isNotEmpty()
            }?.get(0)
        }
    }

    fun isOnline(): Boolean {
        val currentNetWork = getCurrentNetWork()
        val capabilities = connectivityManager?.getNetworkCapabilities(currentNetWork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}