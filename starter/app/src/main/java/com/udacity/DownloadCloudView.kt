package com.udacity

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat.getColor

class DownloadCloudView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint().apply {
        // Smooth out edges of what is drawn without affecting shape.
        isAntiAlias = true
    }

    private val path = Path()
//    private val columnOne = rectInset
//    private val columnTwo = columnOne + rectInset + clipRectRight
//
//    private val rowOne = rectInset
//    private val rowTwo = rowOne + rectInset + clipRectBottom
//    private val rowThree = rowTwo + rectInset + clipRectBottom
//    private val rowFour = rowThree + rectInset + clipRectBottom
//    private val textRow = rowFour + (1.5f * clipRectBottom)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.setBackgroundColor(getColor(this.context,R.color.violate))
      drawBackGroundCloud(canvas)
      drawBackGroundRectangular(canvas)
      drawDownArrow(canvas)
    }

    private fun drawBackGroundRectangular(canvas: Canvas) {
        canvas.save()
        paint.color = getColor(this.context,R.color.violate)
        paint.style = Paint.Style.FILL
        canvas.drawRect(30F, 235F, 1000F, 1000F, paint);
        canvas.restore()
    }

    private fun drawDownArrow(canvas: Canvas) {
        canvas.save()
        paint.color = getColor(this.context,R.color.violate)

        canvas.drawRect(470F, 80F, 530F, 150F, paint);
//        canvas.drawArc(400F,80F,600F,200F,paint)

        path.moveTo(450F,150F)
        path.lineTo(500F,200F)
        path.lineTo(550F,150F)
        path.close()
        canvas.drawPath(path,paint)

        canvas.restore()
    }

    private fun drawBackGroundCloud(canvas: Canvas) {
        canvas.save()

        paint.color = getColor(this.context,R.color.purple)
        canvas.drawCircle(400F,200F,100F,paint)
        canvas.drawCircle(650F,200F,80F,paint)
        canvas.drawCircle(500F,150F,130F,paint)

        canvas.restore()
    }
}