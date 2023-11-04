package com.kaanf.codebaseiocase.ui

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.kaanf.codebaseiocase.R

class DividerDecoration(private val dividerColor: Int) : RecyclerView.ItemDecoration() {
    private val dividerPaint = Paint().apply {
        color = dividerColor
        strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, recyclerView, state)

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val left = child.left + recyclerView.left
            val right = child.right + recyclerView.right
            val top = child.top + recyclerView.top
            val bottom = child.bottom + recyclerView.bottom

            canvas.drawLine(left.toFloat(), bottom.toFloat(), right.toFloat(), bottom.toFloat(), dividerPaint)
        }
    }
}