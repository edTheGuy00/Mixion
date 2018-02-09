package com.taskail.mixion.markortexteditor

import android.text.ParcelableSpan
import java.util.regex.Matcher

/**
 *Created by ed on 2/8/18.
 */

interface ParcelableSpanCreator {
    fun create(matcher: Matcher, iM: Int): ParcelableSpan
}
