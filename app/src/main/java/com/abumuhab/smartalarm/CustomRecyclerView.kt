package com.abumuhab.smartalarm

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val path = Path()
        path.moveTo(0F,0F)
        path.cubicTo(0F,0F,0F,20F,20F,20F)

        path.lineTo((width*0.6).toFloat(), 20F)
        path.quadTo((width*0.6).toFloat(), 20F,(width*0.6+50).toFloat(),80F)
        path.lineTo(width.toFloat(),80F)
        path.lineTo(width.toFloat(),0F)

        canvas.clipPath(path)
        canvas.drawColor(Color.WHITE)
    }
}