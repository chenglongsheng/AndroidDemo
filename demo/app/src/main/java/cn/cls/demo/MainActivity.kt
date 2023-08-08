package cn.cls.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val indicatorSeekBar = findViewById<IndicatorSeekBar>(R.id.isb)
        indicatorSeekBar.setProgress(30)
    }
}