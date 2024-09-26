package com.example.dspl.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class RoundedImageView : AppCompatImageView {
    private var mCornerRadius = 0f

    constructor(context: Context?) : super(context!!, null)

    constructor(context: Context?, attributes: AttributeSet?) : super(
        context!!, attributes
    )

    override fun onDraw(canvas: Canvas) {
        val myDrawable = drawable
        if (myDrawable != null && myDrawable is BitmapDrawable && mCornerRadius > 0) {
            val paint = myDrawable.paint
            val color = -0x1000000
            val bitmapBounds = myDrawable.getBounds()
            val rectF = RectF(bitmapBounds)
            val saveCount = canvas.saveLayer(
                rectF, null,
                Canvas.ALL_SAVE_FLAG
            )
            imageMatrix.mapRect(rectF)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, paint)

            val oldMode = paint.xfermode
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
            super.onDraw(canvas)
            paint.setXfermode(oldMode)
            canvas.restoreToCount(saveCount)
        } else {
            super.onDraw(canvas)
        }
    }

    fun setCornerRadius(cornerRadius: Float) {
        this.mCornerRadius = cornerRadius
    }
}
