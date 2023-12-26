package com.buyehou.message

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

private const val TAG = "MessageService"

/**
 * @author Rosen
 */
class MessageService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return MessageStub().asBinder()
    }
}