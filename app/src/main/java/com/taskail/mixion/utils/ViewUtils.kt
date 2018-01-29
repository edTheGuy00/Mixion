package com.taskail.mixion.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 *Created by ed on 1/29/18.
 */

fun View.hideSoftKeyboard(){
    val keyboard = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    keyboard.hideSoftInputFromWindow(this.windowToken, 0)
}