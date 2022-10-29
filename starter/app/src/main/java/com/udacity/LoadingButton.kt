package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var ovalAngle = 0F

    private var buttonString: String
    private lateinit var rectF: RectF
    var startTextX = this.widthSize / 2.toFloat()
    var startTextY = this.heightSize / 2.toFloat()

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
        textAlign = Paint.Align.CENTER
    }

    private val loadingPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        color = context.getColor(R.color.violate)
        widthSize = width
        heightSize = height
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if (old == ButtonState.Clicked && new == ButtonState.Loading) {
            buttonString = context.getString(R.string.button_loading)
            animateArc(ovalAngle, 270F, 5000)

        } else if (old == ButtonState.Loading && new == ButtonState.Completed) {
            animateArc(ovalAngle, 360F, 650)
        }

    }

    fun setButtonStatue(statue: ButtonState) {
//        if (statue == ButtonState.Clicked && buttonState == ButtonState.Loading) {   //for Testing
        if (statue == ButtonState.Clicked && buttonState == ButtonState.Loading || buttonState == statue) {
//            Just Don't do anything'
        } else {
            buttonState = statue
        }
    }

    init {
        buttonString = context.getString(R.string.download)
        buttonState = ButtonState.Completed
        ovalAngle = 0F
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startTextX = w / 2.toFloat()
        startTextY = h / 2.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.cian))
        drawTranslatedText(canvas, buttonString)
    }

    private fun drawTranslatedText(canvas: Canvas?, text: String) {
        canvas?.save()
        val bounds = Rect()
        var textHeight = 0
        var textWidth = 0

        paint.getTextBounds(text, 0, text.length, bounds)

        textHeight = bounds.height()
        textWidth = bounds.width()

        //Drawing Loading Progress bar for custom Button
        canvas?.drawRect(0F, 0F, ovalAngle * width / 360, height.toFloat(), loadingPaint)

        paint.color = Color.WHITE
        canvas?.drawText(text, startTextX, startTextY, paint)

        rectF = RectF(
            startTextX + textWidth / 2.toFloat(),
            startTextY - textHeight.toFloat(),
            startTextX + textWidth / 2.toFloat() + 50F,
            startTextY - textHeight.toFloat() + 50F
        )
        paint.color = context.getColor(R.color.colorAccent)
        canvas?.drawArc(rectF, 0F, ovalAngle, true, paint)  //our oval

        canvas?.restore() // for reviewer is it useful ?  <----------------------------------------
    }

    fun animateArc(
        fromAngle: Float,
        toAngle: Float,
        duration: Long
    ) { //not private so user can specify what he wants
        ValueAnimator.ofFloat(fromAngle, toAngle).apply {
            this.duration = duration
            interpolator = LinearInterpolator()  //it's animate Linearity in fixed speed
            addUpdateListener { valueAnimator ->
                ovalAngle = valueAnimator.animatedValue as Float
                if (ovalAngle == 360F) {
                    buttonString = context.getString(R.string.download)
                    ovalAngle = 0F
                }
                invalidate()
            }
        }.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}