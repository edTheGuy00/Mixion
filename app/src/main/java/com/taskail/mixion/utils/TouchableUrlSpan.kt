package com.taskail.mixion.utils

import android.content.res.ColorStateList
import android.text.TextPaint
import android.text.style.URLSpan

/**
 *Created by ed on 1/27/18.
 */
open class TouchableUrlSpan(url: String,
                       textColor: ColorStateList,
                       pressedBackGroundColor: Int) : URLSpan(url) {

    private val STATE_PRESSED = intArrayOf(android.R.attr.state_pressed)
    private var isPressed: Boolean = false

    private var normalTextColor: Int = textColor.defaultColor
    private var pressedTextColor: Int = textColor.getColorForState(STATE_PRESSED, normalTextColor)
    private var pressedBackgroundColor: Int = pressedBackGroundColor

    fun setPressed(isPressed: Boolean) {
        this.isPressed = isPressed
    }

    override fun updateDrawState(drawState: TextPaint) {
        drawState.color = if (isPressed) pressedTextColor else normalTextColor
        drawState.bgColor = if (isPressed) pressedBackgroundColor else 0
        drawState.isUnderlineText = !isPressed
    }

}