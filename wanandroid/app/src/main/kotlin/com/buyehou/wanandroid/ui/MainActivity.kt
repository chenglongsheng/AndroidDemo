package com.buyehou.wanandroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buyehou.wanandroid.databinding.ActivityMainBinding

/**
 * @author Rosen
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}