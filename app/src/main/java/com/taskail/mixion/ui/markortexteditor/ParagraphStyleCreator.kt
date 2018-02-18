package com.taskail.mixion.ui.markortexteditor

import android.text.style.ParagraphStyle
import java.util.regex.Matcher

/**
 *Markor text Editor from https://github.com/gsantner/markor
 */

interface ParagraphStyleCreator {
    fun create(matcher: Matcher, iM: Int): ParagraphStyle
}