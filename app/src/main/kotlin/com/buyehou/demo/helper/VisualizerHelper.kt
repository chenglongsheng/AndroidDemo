package com.buyehou.demo.helper

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.util.Log
import com.buyehou.demo.R

const val TAG = "VisualizerHelper"

/**
 * @author buyehou
 */
class VisualizerHelper {

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var visualizer: Visualizer

    fun init(context: Context, dataCallback: DataCallback) {
        if (mediaPlayer != null) {
            mediaPlayer.stop()
        }
        if (visualizer != null) {
            visualizer.release()
        }
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.ytkl)
            mediaPlayer.isLooping = true
            mediaPlayer.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                Log.e("yijunwu", "播放出错！")
                false
            }
            visualizer = Visualizer(mediaPlayer.audioSessionId)
            val captureSize = Visualizer.getCaptureSizeRange()[1]
            val captureRate = Visualizer.getMaxCaptureRate() * 3 / 4
            visualizer.setCaptureSize(captureSize)
            visualizer.setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer,
                    waveform: ByteArray,
                    samplingRate: Int
                ) {
                    Log.d(TAG, "onWaveFormDataCapture: $visualizer $waveform $samplingRate")
                    dataCallback.onWaveCall(waveform)
                }

                override fun onFftDataCapture(
                    visualizer: Visualizer,
                    fft: ByteArray,
                    samplingRate: Int
                ) {
                    Log.d(TAG, "onFftDataCapture: $visualizer $fft $samplingRate")
                    dataCallback.onCall(fft)
                }
            }, captureRate, true, true)
            visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED)
            visualizer.setEnabled(true)
        } catch (e: Exception) {
            Log.e("VisualizerHelper", "请检查录音权限")
        }
    }


    interface DataCallback {
        fun onCall(data: ByteArray?)
        fun onWaveCall(data: ByteArray?)
    }

}