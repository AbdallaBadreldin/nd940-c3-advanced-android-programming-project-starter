package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager.LayoutParams
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var ovalAngle = 0F

    private val valueAnimator = ValueAnimator()
    private lateinit var staticLayout: StaticLayout
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

    private val loadingImage =ImageView(context).apply {
        setBackgroundColor(context.getColor(R.color.white))
        widthSize=this@LoadingButton.width
        heightSize=this@LoadingButton.height
    }

    private val loadingPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = resources.getDimension(R.dimen.strokeWidth)
        color = context.getColor(R.color.violate)
        widthSize = width
        heightSize = height
    }

    val loadingRect= Rect(0,0,width,height)

    private val loadingPath = Path()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if (old == ButtonState.Clicked && new == ButtonState.Loading) {
            buttonString = context.getString(R.string.button_loading)
            animateArc(ovalAngle, 270F, 5000)
//            ValueAnimator.ofFloat(0F, 180F).apply {
//                duration = 650
//                interpolator = LinearInterpolator()
//                addUpdateListener { valueAnimator ->
//                    ovalAngle = valueAnimator.animatedValue as Float
//                    invalidate()
//                }
//            }
        } else if (old == ButtonState.Loading && new == ButtonState.Completed) {
            animateArc(ovalAngle, 360F, 650)
        }

//        buttonString=context.getString(R.string.button_loading)
//        invalidate()
//        startAnimatingArc(50F)

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
        var text_height = 0
        var text_width = 0

        paint.getTextBounds(text, 0, text.length, bounds)

        text_height = bounds.height()
        text_width = bounds.width()

        //Drawing Loading Progress bar for custom Button
        canvas?.drawRect(0F,0F,ovalAngle*width/360,height.toFloat(),loadingPaint)

        paint.color = Color.WHITE
        canvas?.drawText(text, startTextX, startTextY, paint)

        rectF = RectF(
            startTextX + text_width / 2.toFloat(),
            startTextY - text_height.toFloat(),
            startTextX + text_width / 2.toFloat() + 50F,
            startTextY - text_height.toFloat() + 50F
        )
        paint.color = context.getColor(R.color.colorAccent)
        canvas?.drawArc(rectF, 0F, ovalAngle, true, paint)  //our oval

        canvas?.restore() // for reviewer is it useful ?  <----------------------------------------
    }

    fun animateArc(fromAngle: Float, toAngle: Float, duration: Long) {
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
//                val mover = ObjectAnimator.ofFloat(loadingImage, View.TRANSLATION_X,height.toFloat(),widthSize.toFloat())
//                mover.interpolator = LinearInterpolator()  //it's animate Linearity in fixed speed
//                mover.duration=duration
            }
        }.start()
    }

   /* private fun drawTranslatedTextAndAnimateOval(canvas: Canvas?, text: String) {

        val paint = Paint()
        val bounds = Rect()
        var text_height = 0
        var text_width = 0

        paint.typeface = Typeface.DEFAULT // your preference here
        paint.textSize =
            resources.getDimension(R.dimen.textSize) // have this the same as your text size
        paint.textAlign = Paint.Align.CENTER
        paint.getTextBounds(text, 0, text.length, bounds)

        text_height = bounds.height()
        text_width = bounds.width()

        paint.color = Color.WHITE
        canvas?.drawText(text, startTextX, startTextY, paint)

    }*/

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