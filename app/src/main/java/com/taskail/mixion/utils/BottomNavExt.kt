package com.taskail.mixion.utils

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.util.Log
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

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disbleShiftMode(){
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView

            item.setShiftingMode(false)
            // set once again checked value, so view will be updated

            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        Log.e("ShiftDisabler", "Unable to get shift mode field", e)
    } catch (e: IllegalAccessException) {
        Log.e("ShiftDisabler", "Unable to change value of shift mode", e)
    }

}