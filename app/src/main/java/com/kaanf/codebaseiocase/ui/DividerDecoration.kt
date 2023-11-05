package com.kaanf.codebaseiocase.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kaanf.codebaseiocase.R

class SimpleDividerItemDecoration(context: Context, @DrawableRes dividerRes: Int) : RecyclerView.ItemDecoration() {

    private val divider: Drawable = ContextCompat.getDrawable(context, R.drawable.divider)!! // Divider'ınızın drawable kaynağı

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // Divider'ları yuvarlatılmış köşelerin içinde kesmeye çalışın.
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left + params.leftMargin
            val right = child.right - params.rightMargin
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}
