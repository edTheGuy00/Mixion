package com.taskail.mixion.markortexteditor

import android.text.ParcelableSpan
import java.util.regex.Matcher

/**
 *Markor text Editor from https://github.com/gsantner/markor
 */

interface ParcelableSpanCreator {
    fun create(matcher: Matcher, iM: Int): ParcelableSpan
}
