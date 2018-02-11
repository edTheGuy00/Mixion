package com.taskail.mixion.markortexteditor.highlighter

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import android.text.Editable
import android.text.ParcelableSpan
import android.text.style.TextAppearanceSpan
import android.util.TypedValue
import com.taskail.mixion.markortexteditor.ParcelableSpanCreator
import java.util.regex.Matcher

/**
 *  Markor text Editor from https://github.com/gsantner/markor
 */

class MarkdownHeaderSpanCreator(private val highlighter: MarkdownHighlighter,
                                private val editable: Editable,
                                private val color: Int) :
        ParcelableSpanCreator {

    private val POUND_SIGN = '#'
    private val DISPLAY_METRICS = Resources.getSystem().displayMetrics
    private val STANDARD_PROPORTION_MAX = 1.80f
    private val SIZE_STEP = 0.20f

    override fun create(matcher: Matcher, iM: Int): ParcelableSpan {
        val charSequence = extractMatchingRange(matcher)
        val proportion = calculateProportionBasedOnHeaderType(charSequence)
        val size = calculateAdjustedSize(proportion)
        return TextAppearanceSpan(highlighter.fontType, Typeface.BOLD, size.toByte().toInt(),
                ColorStateList.valueOf(color), null)
    }

    private fun calculateAdjustedSize(proportion: Float?): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                highlighter.fontSize * proportion!!,
                DISPLAY_METRICS)
    }

    private fun extractMatchingRange(m: Matcher): CharArray {
        return editable.subSequence(m.start(), m.end()).toString().trim({ it <= ' ' }).toCharArray()
    }

    private fun calculateProportionBasedOnHeaderType(charSequence: CharArray): Float? {

        val proportion = calculateProportionForHashesHeader(charSequence)
        return if (proportion == STANDARD_PROPORTION_MAX) {
            calculateProportionForUnderlineHeader(charSequence)
        } else proportion
    }

    private fun calculateProportionForUnderlineHeader(charSequence: CharArray): Float? {
        var proportion: Float? = STANDARD_PROPORTION_MAX
        if (Character.valueOf('=') == charSequence[charSequence.size - 1]) {
            proportion = proportion?.minus(SIZE_STEP)
        } else if (Character.valueOf('-') == charSequence[charSequence.size - 1]) {
            proportion = proportion?.minus(SIZE_STEP.times( 2))
        }
        return proportion
    }

    private fun calculateProportionForHashesHeader(charSequence: CharArray): Float? {
        var proportion = STANDARD_PROPORTION_MAX
        var i = 0
        // Reduce by SIZE_STEP for each #
        while (POUND_SIGN == charSequence[i]) {
            proportion -= SIZE_STEP
            i++
        }
        return proportion
    }

}