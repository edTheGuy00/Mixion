package com.taskail.mixion.markortexteditor.highlighter

import android.text.TextPaint
import android.text.style.UnderlineSpan
import android.text.style.UpdateAppearance

/**
 *  Markor text Editor from https://github.com/gsantner/markor
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