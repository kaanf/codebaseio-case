package com.kaanf.codebaseiocase.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kaanf.codebaseiocase.R
import com.kaanf.codebaseiocase.utils.setStatusBarTransparent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    var statusBarHeight: Int = 0
    var navigationBarHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarTransparent()

        setContentView(R.layout.activity_main)

        setStatusBarHeight()
        setNavigationBarHeight()
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.navHostFragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun setStatusBarHeight() {
        val idStatusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")

        statusBarHeight = if (idStatusBarHeight > 0)
            resources.getDimensionPixelSize(idStatusBarHeight)
        else
            60
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun setNavigationBarHeight() {
        val idNavigationBarHeight = resources.getIdentifier("navigation_bar_height", "dimen", "android")

        navigationBarHeight = if (idNavigationBarHeight > 0)
            resources.getDimensionPixelSize(idNavigationBarHeight)
        else
            0
    }
}