package com.example.poidem_gulyat.utils.customView

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.graphics.toRegion
import com.example.poidem_gulyat.R

@SuppressLint("Recycle")
class CustomTitleFilter(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val paint: Paint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 7f
        style = Paint.Style.STROKE
        flags = Paint.ANTI_ALIAS_FLAG
    }

    private var text: String = "Сортировать по"
    var setPaintBlackStroke = false



    var radius: Float = 0f
    var radiusForAnim: Float = 0f


    var midWidth: Float = 0f
    var midHeight: Float = 0f
    var leftWidth: Float = 0f
    var rightWidth: Float = 0f
    var topHight: Float = 0f
    var bottomHight: Float = 0f
    private var sliceRect3 = RectF()
    private var isAnimate = false
    private var isDraw = false
    var listener: ClickListener? = null
    var listenerDraw: DrawListener? = null

    fun setlistener(listener: ClickListener) {
        this.listener = listener
    }

    private val paintWhite: Paint = Paint().apply {
        color = Color.BLACK
        textSize = 30f * context.resources.displayMetrics.density
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
        flags = Paint.ANTI_ALIAS_FLAG
    }


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleFilter)
        paintWhite.textSize =
            typedArray.getDimension(R.styleable.CustomTitleFilter_CustomTitleFilter_textSize,
                14 * context.resources.displayMetrics.density)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val fm: Paint.FontMetrics = paintWhite.fontMetrics
        val textHeight = fm.bottom - fm.top + fm.leading;
        val textWidth = paintWhite.measureText(text)
        when (widthMode) {
            MeasureSpec.AT_MOST -> setMeasuredDimension(
                ((textWidth + 150)).toInt(),
                ((textHeight + 60)).toInt()
            )
            MeasureSpec.EXACTLY -> {
                Log.d(TAG, "onMeasure EXACTLY")
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        radius = ((right - left) / 2).toFloat()
        radiusForAnim = radius


        midWidth = ((right - left) / 2).toFloat()
        midHeight = ((bottom - top) / 2).toFloat()
        leftWidth = radius / 90
        rightWidth = 2 * radius - radius / 90
        topHight = midHeight / 10
        bottomHight = 2 * midHeight - midHeight / 10

        super.onLayout(changed, left, top, right, bottom)
    }

    var firstTimeDraw = true

    private fun Canvas.setRectForNameSlice() {
        sliceRect3.set(
            leftWidth,
            topHight,
            rightWidth,
            bottomHight
        )
    }

    private fun Paint.getTextBaseLineByCenter(center: Float) = center + descent()

    private fun Canvas.setRectForAnySpending() {
        drawRoundRect(
            sliceRect3,
            200f,
            200f,
            paint
        )
        // p.reset()
        val pathLine = Path()
        val pathLineCewnter = Path()
        val fm: Paint.FontMetrics = paintWhite.fontMetrics
        val textHeight = fm.bottom - fm.top + fm.leading;
        pathLine.moveTo(
            0f,
            paintWhite.getTextBaseLineByCenter(midHeight)
        )
        pathLine.lineTo(
            midWidth + midWidth,
            paintWhite.getTextBaseLineByCenter(midHeight)
        )

        pathLineCewnter.moveTo(
            0f,
            midHeight
        )
        pathLineCewnter.lineTo(
            paintWhite.measureText(text),
            midHeight
        )
        drawTextOnPath(text, pathLine, 0f, 0f, paintWhite)
//        drawPath(pathLineCewnter, paint)
//        drawPath(pathLine, paint2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        isDraw = false
        canvas.setRectForNameSlice()
        //  canvas.setChartBody()
        canvas.setRectForAnySpending()

        isDraw = true
        if (firstTimeDraw)
            listenerDraw?.drawDone()
        firstTimeDraw = false
    }

    fun setValues(item: String) {
        text = item
        requestLayout()
        invalidate()
    }

    fun setPaintBlackFull(){
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paintWhite.color = Color.WHITE
    }

    fun setPaintBlackStroke(){
        paint.color = Color.BLACK
        setPaintBlackStroke = true
    }

    fun startAnimation() {

        if (isDraw) {
            isAnimate = true
            ValueAnimator.ofFloat(0.toFloat(), midWidth).apply {
                duration = 500
                repeatCount = 0
                repeatMode = ValueAnimator.RESTART
                addUpdateListener {
                    midWidth = it.animatedValue as Float
                    invalidate()
                }
                start()
            }.doOnEnd {
                isAnimate = false
            }
            isAnimate = true
            ValueAnimator.ofFloat(0.toFloat(), radiusForAnim).apply {
                duration = 500
                repeatCount = 0
                repeatMode = ValueAnimator.RESTART
                addUpdateListener {
                    radiusForAnim = it.animatedValue as Float
                    invalidate()
                }
                start()
            }.doOnEnd {
                isAnimate = false
            }
        }
    }

    fun isActive(flag:Int){
        if(flag == 0){
            paint.color = Color.GRAY
        } else{
            paint.color = Color.BLACK
        }
        invalidate()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isDraw) {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    if(!setPaintBlackStroke)
                    if (paint.color == Color.GRAY)
                        paint.color = Color.BLACK else paint.color = Color.GRAY
                    listener!!.onClick()
                    invalidate()
                }
            }
        }
        return true
    }

    fun interface ClickListener {
        fun onClick()
    }

    companion object {
        const val TAG = "CustomView"
    }
}

interface DrawListener {
    fun drawDone()
}