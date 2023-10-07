package com.buyehou.toolbox

import android.app.Application
import android.content.Context
import android.util.Log

/**
 * @author Rosen
 * @date 2023/10/7 14:17
 */
class Toolbox private constructor() {
    fun init(application: Application) {
        Log.d(TAG, "isInit: $isInit")
        if (isInit) {
            return
        }
        isInit = true
        sApplication = application
    }

    val globalContext: Context
        get() {
            if (sApplication == null) {
                throw NullPointerException("need application to init")
            }
            return sApplication!!.applicationContext
        }

    companion object {
        private const val TAG = "Toolbox"

        @Volatile
        private var sInstance: Toolbox? = null
        private var sApplication: Application? = null
        private var isInit = false
        val instance: Toolbox
            get() {
                if (sInstance == null) {
                    synchronized(Toolbox::class.java) {
                        if (sInstance == null) {
                            sInstance = Toolbox()
                        }
                    }
                }
                return sInstance!!
            }
    }
}