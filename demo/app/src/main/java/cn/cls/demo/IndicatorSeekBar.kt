package cn.cls.demo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

private const val TAG = "IndicatorSeekBar"

/**
 * @author Rosen
 * @date 2023/8/1 16:21
 */
class IndicatorSeekBar : ConstraintLayout, SeekBar.OnSeekBarChangeListener {

    private lateinit var seekBar: SeekBar

    private lateinit var progress: TextView

    private var listener: OnSeekBarChangeListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_indicator_seekbar, this)
        seekBar = view.findViewById(R.id.seek_bar)
        progress = view.findViewById(R.id.progress)
        seekBar.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        listener?.onProgressChanged(seekBar, progress, fromUser)
        this.progress.text = progress.toString()
        updateValuePosition()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        listener?.onStartTrackingTouch(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        listener?.onStopTrackingTouch(seekBar)
    }

    fun setOnSeekBarChangeListener(listener: OnSeekBarChangeListener) {
        this.listener = listener
    }

    fun setProgress(progress: Int) {
        this.progress.text = progress.toString()
        this.seekBar.progress = progress
        post {
            updateValuePosition()
        }
    }

    fun getProgress(): Int {
        return this.seekBar.progress
    }

    fun setMax(max: Int) {
        this.seekBar.max = max
    }

    fun getMax(): Int {
        return this.seekBar.max
    }

    private fun updateValuePosition() {
        this.seekBar.let {
            val fl =
                (it.thumb.intrinsicWidth - this.progress.width) / 2 + it.thumb.bounds.left + it.x
            this.progress.x = fl
        }
    }

    interface OnSeekBarChangeListener {
        fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean)
        fun onStartTrackingTouch(seekBar: SeekBar?)
        fun onStopTrackingTouch(seekBar: SeekBar?)
    }

}