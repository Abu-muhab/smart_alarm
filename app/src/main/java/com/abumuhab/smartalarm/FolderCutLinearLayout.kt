package com.abumuhab.smartalarm

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.ListView
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.RecyclerView

class FolderCutLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var cutPercent = 0.0f
    private var cutOutColor = 0
    private var cutHeight = 0f
    private var invertCutTopRightCorner = false

    init {
        context.withStyledAttributes(attrs, R.styleable.FolderCutLinearLayout) {
            cutPercent = getFloat(R.styleable.FolderCutLinearLayout_cutPercent, 0.6f)
            cutOutColor = getColor(R.styleable.FolderCutLinearLayout_cutOutColor, 0)
            cutHeight = getFloat(R.styleable.FolderCutLinearLayout_cutHeight, 80f)
            invertCutTopRightCorner =
                getBoolean(R.styleable.FolderCutLinearLayout_invertCutTopRightCorner, false)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val path = Path()
        path.moveTo(0F, 0F)
        if (!invertCutTopRightCorner) {
            path.cubicTo(0F, 0F, 0F, 20F, 20F, 20F)
            path.lineTo(width * cutPercent, 20F)
            path.quadTo(width * cutPercent, 20F, width * cutPercent + 50, cutHeight)
        } else {
            path.lineTo(width * cutPercent, 0F)
            path.quadTo(width * cutPercent, 0F, width * cutPercent + 50, cutHeight)
        }

        path.lineTo(width.toFloat(), cutHeight)
        path.lineTo(width.toFloat(), 0F)

        canvas.clipPath(path)
        canvas.drawColor(cutOutColor)
    }
}