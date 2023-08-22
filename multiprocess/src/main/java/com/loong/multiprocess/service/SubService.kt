package com.loong.multiprocess.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author Rosen
 * @date 2023/8/22 18:08
 */
class SubService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}