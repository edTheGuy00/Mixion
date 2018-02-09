package com.taskail.mixion.markortexteditor

import android.text.TextPaint
import android.text.style.UnderlineSpan
import android.text.style.UpdateAppearance

/**
 *Created by ed on 2/8/18.
 */

class ColorUnderlineSpan (val color: Int,
                          val thickness: Float? = 1.0f) : UnderlineSpan(), UpdateAppearance{

    override fun updateDrawState(tp: TextPaint) {
        try {
            val method = TextPaint::class.java.getMethod("setUnderlineText", Integer.TYPE, java.lang.Float.TYPE)
            method.invoke(tp, color, thickness)
        } catch (e: Exception) {
            tp.isUnderlineText = true
        }

    }

}