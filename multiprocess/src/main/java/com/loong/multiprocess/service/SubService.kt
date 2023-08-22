package com.loong.multiprocess.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.loong.multiprocess.constant.MsgConst

/**
 * @author Rosen
 * @date 2023/8/22 18:08
 */
class SubService : Service() {
    companion object {
        private const val TAG = "SubService"

        private var otherMessenger: Messenger? = null

        /**
         * 发送消息
         */
        fun sendMsg(msg: Message) {
            Log.d(TAG, "sendMsg: msg[$msg] other[$otherMessenger]")
            otherMessenger?.send(msg)
        }

    }

    private val messageHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            handleMsg(msg)
        }
    }

    private val selfMessenger = Messenger(messageHandler)

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "onServiceConnected: name[$name] service[$service]")
            // 与主进程服务连接上的时候，初始化对向的messenger
            if (otherMessenger == null) {
                otherMessenger = Messenger(service)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "onServiceDisconnected: name[$name]")
            otherMessenger = null
            bindOther()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return selfMessenger.binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        bindOther()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: intent[$intent] flags[$flags] startId[$startId]")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    private fun bindOther() {
        val intent = Intent(this, MainService::class.java)
        startService(intent)
        bindService(intent, conn, BIND_AUTO_CREATE)
    }

    private fun handleMsg(msg: Message) {
        Log.d(TAG, "handleMsg: msg[${msg.what}]")
        when (msg.what) {
            MsgConst.MAIN2SUB.YSN_DATA -> {

            }

            MsgConst.MAIN2SUB.YSN_LIVE_DATA -> {

            }
        }
    }

}