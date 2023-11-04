package com.kaanf.codebaseiocase.utils

import android.app.Activity
import android.os.Build
import android.view.View

fun Activity.setStatusBarTransparent() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    else
        window.setDecorFitsSystemWindows(false)
}