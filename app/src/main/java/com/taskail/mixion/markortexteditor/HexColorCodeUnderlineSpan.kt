package com.taskail.mixion.markortexteditor

import android.graphics.Color
import android.text.ParcelableSpan
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *Created by ed on 2/8/18.
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