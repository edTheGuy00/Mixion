package com.taskail.mixion.markortexteditor.highlighter

import android.graphics.Color
import android.text.ParcelableSpan
import com.taskail.mixion.markortexteditor.ParcelableSpanCreator
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *  Markor text Editor from https://github.com/gsantner/markor
 */
class HexColorCodeUnderlineSpan : ParcelableSpanCreator {

    companion object {
        @JvmStatic
        internal val PATTERN = Pattern.compile("(?:\\s|^)(#[A-Fa-f0-9]{6,8})+(?:\\s|$)")
    }

    override fun create(matcher: Matcher, iM: Int): ParcelableSpan {
        return ColorUnderlineSpan(Color.parseColor(matcher.group(1)), null)

    }
}