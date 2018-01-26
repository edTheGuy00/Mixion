package com.taskail.mixion.utils

import android.support.design.widget.BottomNavigationView
import android.view.View
import android.view.animation.AccelerateInterpolator

/**
 *Created by ed on 1/25/18.
 */


fun BottomNavigationView.hideBottomNavigationView(){
    this.animate().translationY(this.height.toFloat()).setInterpolator(AccelerateInterpolator(2f))
            .withEndAction { this.visibility = View.GONE }.start()
}

fun BottomNavigationView.showBottomNavigationView(){
    this.animate().translationY(0f).interpolator = AccelerateInterpolator(2f)
    this.visibility = View.VISIBLE
}