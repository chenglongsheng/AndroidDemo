package com.loong.multiprocess

import android.app.Application
import android.util.Log

private const val TAG = "MultiProcessApplication"

/**
 * @author Rosen
 * @date 2023/9/27 10:16
 */
class MultiProcessApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

}