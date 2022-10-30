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
    private var valueAnimater = ValueAnimator()

    private lateinit var buttonString: String
    private lateinit var rectF: RectF
    var startTextX = this.widthSize / 2.toFloat()
    var startTextY = this.heightSize / 2.toFloat()
    var duration: Int = 5000

    var text: String = ""
    var loadingText: String = ""
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
            buttonString = loadingText
            animateArc(ovalAngle, 270F, duration = duration.toLong())

        } else if (old == ButtonState.Loading && new == ButtonState.Completed) {
            valueAnimater.removeAllUpdateListeners()
            valueAnimater.cancel()
            animateArc(ovalAngle, 360F, 650)
        }

    }

    fun setButtonStatue(statue: ButtonState) {
        if (statue == ButtonState.Clicked && buttonState == ButtonState.Loading || buttonState == statue) {
//            Just Don't do anything'
        } else {
            buttonState = statue
        }
    }

    init {
        buttonState = ButtonState.Completed
        ovalAngle = 0F
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0, 0
        ).apply {
            try {
                text = getString(R.styleable.LoadingButton_text).toString()
                loadingText = getString(R.styleable.LoadingButton_loadingText).toString()
                duration = getInt(R.styleable.LoadingButton_duration, 5000)
                buttonString = text
            } finally {
                recycle()
            }
        }
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
    }

    fun animateArc(
        fromAngle: Float,
        toAngle: Float,
        duration: Long
    ) { //not private so user can specify what he wants
        valueAnimater.setFloatValues(fromAngle, toAngle)
        valueAnimater.duration = duration
        valueAnimater.interpolator = LinearInterpolator() //it's animate Linearity in fixed speed
        valueAnimater.addUpdateListener { valueAnimator ->
            ovalAngle = valueAnimator.animatedValue as Float
            if (ovalAngle >= 360F) {
                buttonString = text
                ovalAngle = 0F
            }
            invalidate()
        }
        valueAnimater.start()
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