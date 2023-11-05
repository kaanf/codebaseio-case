package com.kaanf.codebaseiocase.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager

fun Activity.setStatusBarTransparent() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    else
        window.setDecorFitsSystemWindows(false)
}