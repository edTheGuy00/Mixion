package com.taskail.mixion.markortexteditor

import android.text.style.ParagraphStyle
import java.util.regex.Matcher

/**
 *Created by ed on 2/8/18.
 */

interface ParagraphStyleCreator {
    fun create(matcher: Matcher, iM: Int): ParagraphStyle
}