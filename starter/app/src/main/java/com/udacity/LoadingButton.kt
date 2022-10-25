package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()
    private lateinit var staticLayout: StaticLayout

    var startTextX = this.widthSize / 2.toFloat()
    var startTextY = this.heightSize / 2.toFloat()

    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        textSize = resources.getDimension(R.dimen.textSize)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }


    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startTextX = w / 2.toFloat()
        startTextY = h / 2.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.setBackgroundColor(ContextCompat.getColor(this.context, R.color.cian))
        drawTranslatedTextExample(canvas, context.getString(R.string.download))

    }

    private fun drawTranslatedTextExample(canvas: Canvas?, text: String) {
        canvas?.save()
        val paint = Paint()
        val bounds = Rect()
        var text_height = 0
        var text_width = 0

        paint.typeface = Typeface.DEFAULT // your preference here
        paint.textSize = resources.getDimension(R.dimen.textSize) // have this the same as your text size
        paint.textAlign = Paint.Align.CENTER
        paint.getTextBounds(text, 0, text.length, bounds)

        text_height = bounds.height()
        text_width = bounds.width()

        paint.color = Color.WHITE
        canvas?.drawText(text, startTextX, startTextY, paint)

        canvas?.restore()
    }

    /* private fun drawTranslatedTextExample(canvas: Canvas?, text: String) {
         canvas?.save()

         *//*  val p = paint.apply {
              color = Color.GREEN
              // Align the RIGHT side of the text with the origin.
              textAlign = Paint.Align.CENTER
          }  *//*

        val textPaintCopy = TextPaint(TextPaint.FAKE_BOLD_TEXT_FLAG)
        textPaintCopy.apply {
            textSize = resources.getDimension(R.dimen.textSize)
            color = Color.WHITE
            textAlign=Paint.Align.CENTER
            textAlignment=  TEXT_ALIGNMENT_CENTER
        }

        val staticLayout = StaticLayout.Builder
            .obtain(
                text,
                0,
                text.length,
                textPaintCopy,
                widthSize
            )
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .build()

        // Draw text.
//        canvas?.drawText(
//            text,
//            startTextX, startTextY, paint
//        )

        TextView(this.context).apply {
            this.text = text
            setTextColor(Color.WHITE)
            textSize = resources.getDimension(R.dimen.textSize)
//            layout.alignment=Layout.Alignment.ALIGN_CENTER
        }
//        canvas?.translate(startTextX, startTextY)

        staticLayout.draw(canvas)
        canvas?.restore()
    }*/

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
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