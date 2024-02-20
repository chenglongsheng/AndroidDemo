package com.buyehou.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buyehou.demo.databinding.ActivityMainBinding
import com.buyehou.demo.helper.VisualizerHelper

/**
 * @author Rosen
 */
class MainActivity : AppCompatActivity() {

    private lateinit var visualizerHelper: VisualizerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        visualizerHelper = VisualizerHelper()

        binding.play.setOnClickListener {
            binding.effect.setSurroundEffectDrawable()
        }

    }

    private fun play() {
        visualizerHelper.init(this, object : VisualizerHelper.DataCallback {
            override fun onCall(data: ByteArray?) {
            }

            override fun onWaveCall(data: ByteArray?) {
            }
        })
    }

}