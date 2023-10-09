package com.loong.widget.bar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.loong.widget.R
import com.loong.widget.utils.getColor
import kotlin.math.pow
import kotlin.math.sqrt

private const val TAG = "CustomSeekBar"

/**
 * @author Rosen
 * @date 2023/8/10 19:30
 */
class CustomSeekBar : View {

    private var radius = 0f

    private var thumbInterval = 18f

    private var bgStartColor = getColor(R.color.bg_start)
    private var bgEndColor = getColor(R.color.bg_end)

    private var bgTrackStartColor = getColor(R.color.bg_track_start)
    private var bgTrackEndColor = getColor(R.color.bg_track_end)

    private var thumbStartColor = Color.WHITE

    private var thumbEndColor = Color.WHITE

    private val mPaint = Paint()

    private var startX = 0f
    private var startY = 0f
    private var dx = 0f

    private var textLeft = 240f
    private var seekTextColor = Color.WHITE
    private var seekText = ""
    private var seekTextSize = 40f
    private var mTextPaint = Paint()

    private lateinit var path: Path

    private var canChange = false

    var progress = 0
        set(value) {
            field = value
            postInvalidate()
        }

    private var processBlock: ((Int) -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
        initDefault(attrs)
    }

    private fun initView() {
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true

        textLeft = dp2px(textLeft)
        mTextPaint.textAlign = Paint.Align.LEFT
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = sp2px(seekTextSize)
        isClickable = true

        thumbInterval = dp2px(thumbInterval)
        path = Path()
    }

    private fun initDefault(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomSeekBar
        )
        bgStartColor =
            typedArray.getColor(R.styleable.CustomSeekBar_bgStartColor, getColor(R.color.bg_start))
        bgEndColor =
            typedArray.getColor(R.styleable.CustomSeekBar_bgEndColor, getColor(R.color.bg_end))
        bgTrackStartColor =
            typedArray.getColor(
                R.styleable.CustomSeekBar_bgStartColor,
                getColor(R.color.bg_track_start)
            )
        bgTrackEndColor =
            typedArray.getColor(
                R.styleable.CustomSeekBar_bgTrackEndColor,
                getColor(R.color.bg_track_end)
            )
        thumbStartColor =
            typedArray.getColor(R.styleable.CustomSeekBar_thumbStartColor, Color.WHITE)
        thumbEndColor =
            typedArray.getColor(R.styleable.CustomSeekBar_thumbEndColor, Color.WHITE)
        seekTextColor =
            typedArray.getColor(R.styleable.CustomSeekBar_seekTextColor, Color.WHITE)
        mTextPaint.textSize =
            typedArray.getDimension(R.styleable.CustomSeekBar_seekTextSize, sp2px(40f))
        seekText = typedArray.getString(R.styleable.CustomSeekBar_seekText) ?: ""
        seekTextColor = typedArray.getColor(
            R.styleable.CustomSeekBar_seekTextColor,
            Color.WHITE
        )
        textLeft =
            typedArray.getDimension(R.styleable.CustomSeekBar_seekTextMarginLeft, dp2px(240f))
        thumbInterval = typedArray.getDimension(R.styleable.CustomSeekBar_thumbInterval, dp2px(18f))
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure: $widthMeasureSpec $heightMeasureSpec")
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val wSize = MeasureSpec.getSize(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val hSize = MeasureSpec.getSize(heightMeasureSpec)

        // 设置视图的默认宽度
        val desiredWidth =
            dp2px(800f).toInt() + paddingLeft + paddingRight + marginLeft + marginRight
        // 设置视图的默认高度
        val desiredHeight =
            dp2px(200f).toInt() + paddingTop + paddingBottom + marginTop + marginBottom

        val width: Int = when (wMode) {
            MeasureSpec.EXACTLY -> {
                wSize
            }

            MeasureSpec.AT_MOST -> {
                wSize.coerceAtMost(desiredWidth)
            }

            else -> {
                desiredWidth
            }
        }

        val height: Int = when (hMode) {
            MeasureSpec.EXACTLY -> {
                hSize
            }

            MeasureSpec.AT_MOST -> {
                hSize.coerceAtMost(desiredHeight)
            }

            else -> {
                desiredHeight
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        radius = height.toFloat() / 2
        drawBg(canvas)
        drawTrack(canvas)
        drawText(canvas)
    }

    private fun drawBg(canvas: Canvas) {
        // 左半圆
        val left = 0f
        val top = 0f
        path.addArc(left, top, height.toFloat(), height.toFloat(), 90f, 180f)
        // 中间矩形
        val rectLeft = left + radius
        val rectWidth = width - height
        path.addRect(rectLeft, top, rectLeft + rectWidth, height.toFloat(), Path.Direction.CW)
        // 右半圆
        val rightArcLeft = rectLeft + rectWidth - radius
        path.addArc(rightArcLeft, top, rightArcLeft + radius * 2, height.toFloat(), -90f, 180f)
        mPaint.shader = LinearGradient(
            left, height.toFloat() / 2,
            width.toFloat(), height.toFloat() / 2,
            bgStartColor,
            bgEndColor,
            Shader.TileMode.CLAMP
        )
        canvas.drawPath(path, mPaint)
        path.reset()
    }

    private fun drawTrack(canvas: Canvas) {
        // 左半圆
        val left = dx
        val top = 0f
        path.addArc(left, top, height.toFloat() + left, height.toFloat(), 90f, 180f)
        // 中间矩形
        val rectLeft = left + radius
        val rectWidth = width - height
        path.addRect(rectLeft, top, radius + rectWidth, height.toFloat(), Path.Direction.CW)
        // 右半圆
        val rightArcLeft = rectWidth.toFloat()
        path.addArc(rightArcLeft, top, rightArcLeft + radius * 2, height.toFloat(), -90f, 180f)
        mPaint.shader = LinearGradient(
            left, height.toFloat() / 2,
            width.toFloat(), height.toFloat() / 2,
            bgTrackStartColor,
            bgTrackEndColor,
            Shader.TileMode.CLAMP
        )
        canvas.drawPath(path, mPaint)
        path.reset()
        // 小圆
//        mPaint.shader = LinearGradient(
//            left, radius, left + height, radius,
//            thumbStartColor,
//            thumbEndColor,
//            Shader.TileMode.CLAMP
//        )
        mPaint.reset()
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.WHITE
        canvas.drawCircle(
            left + radius,
            height.toFloat() / 2,
            radius - thumbInterval,
            mPaint
        )
    }

    private fun drawText(canvas: Canvas) {
        val textWidth = mTextPaint.measureText(seekText)
        val textRight = textWidth + textLeft
        val thumbPosition = radius + radius - thumbInterval
        if (dx + thumbPosition > textLeft) {
            mTextPaint.shader = LinearGradient(
                dx + thumbPosition,
                height.toFloat() / 2,
                dx + thumbPosition + 1,
                height.toFloat() / 2,
                Color.TRANSPARENT,
                seekTextColor,
                Shader.TileMode.CLAMP
            )
        } else {
            mTextPaint.shader = LinearGradient(
                textLeft, height.toFloat() / 2, textRight, height.toFloat() / 2,
                seekTextColor, seekTextColor,
                Shader.TileMode.CLAMP
            )
        }
        mTextPaint.color = seekTextColor
        canvas.drawText(
            seekText,
            textLeft,
            (height / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2),
            mTextPaint
        )
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dp2px(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            resources.displayMetrics
        )
    }

    private fun sp2px(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value,
            resources.displayMetrics
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "onTouchEvent: x::${event.x} y::${event.y}")
                if (isTouchInsideCircle(event.x, event.y)) {
                    canChange = true
                    startX = event.x
                    startY = event.y
                } else {
                    canChange = false
                }
            }

            MotionEvent.ACTION_UP -> {
                if (canChange) {
                    dx = 0f
                    invalidate()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (!canChange) return false
                dx = event.x - startX
                if (dx > (width - height)) {
                    dx = (width - height).toFloat()
                }
                if (dx < 0) {
                    dx = 0f
                }
                processBlock?.invoke((dx * 100 / (width - height)).toInt())
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    fun seekProgressListener(processBlock: (Int) -> Unit) {
        this.processBlock = processBlock
    }

    fun reset() {
        dx = 0f
        invalidate()
    }

    private fun isTouchInsideCircle(x: Float, y: Float): Boolean {
        val cx = dx + this.radius
        val cy = this.radius
        val radius = this.radius - thumbInterval
        val distance = sqrt((x - cx).toDouble().pow(2.0) + (y - cy).toDouble().pow(2.0))
        return distance <= radius
    }

}