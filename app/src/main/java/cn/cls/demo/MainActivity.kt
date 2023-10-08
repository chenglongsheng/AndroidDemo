package cn.cls.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import java.io.IOException

private const val DEBUG_TAG = "NetworkStatusExample"

private const val TAG = DEBUG_TAG

class MainActivity : AppCompatActivity() {

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connMgr.activeNetwork
            val networkInfo = connMgr.getNetworkInfo(activeNetwork)
            val networkCapabilities = connMgr.getNetworkCapabilities(activeNetwork)
            val linkProperties = connMgr.getLinkProperties(activeNetwork)
            Log.d(TAG, "networkInfo: $networkInfo")
            Log.d(TAG, "networkCapabilities: $networkCapabilities")
            Log.d(TAG, "linkProperties: $linkProperties")
        }
    }

    private val netWorkCallback = object :
        ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            Log.e(TAG, "The default network is now: $network")
        }

        override fun onLost(network: Network) {
            Log.e(
                TAG,
                "The application no longer has a default network. The last default network was $network"
            )
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            Log.e(TAG, "The default network changed capabilities: $networkCapabilities")
        }

        override fun onLinkPropertiesChanged(
            network: Network,
            linkProperties: LinkProperties
        ) {
            Log.e(TAG, "The default network changed link properties: $linkProperties")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://www.wanandroid.com/article/list/1/json")
            .method("GET", null)
            .build()
        Log.d(TAG, "headers: ${request.headers()}")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "onResponse: $response")
                val body = response.body() ?: return
                Log.d(TAG, "onResponse: ${body.contentType()}")
                Log.d(TAG, "onResponse: ${body.string()}")
            }
        })
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifiConn = false
        var isMobileConn = false
        connMgr.activeNetwork
        connMgr.allNetworks.forEach { network ->
            connMgr.getNetworkInfo(network).apply {
                if (this?.type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (this?.type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }
        Log.d(DEBUG_TAG, "Wifi connected: $isWifiConn")
        Log.d(DEBUG_TAG, "Mobile connected: $isMobileConn")
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(netWorkCallback)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.unregisterNetworkCallback(netWorkCallback)
    }

}