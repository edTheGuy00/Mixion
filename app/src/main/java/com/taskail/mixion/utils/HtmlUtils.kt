package com.taskail.mixion.utils

import android.widget.TextView

/**
 *Created by ed on 1/27/18.
 */


/**
 * Work around some 'features' of TextView and URLSpans. i.e. vanilla URLSpans do not react to
 * touch so we replace them with our own [TouchableUrlSpan]
 * & [LinkTouchMovementMethod] to fix this.
 *
 *
 * Setting a custom MovementMethod on a TextView also alters touch handling (see
 * TextView#fixFocusableAndClickableSettings) so we need to correct this.
 */
fun setTextWithNiceLinks(textView: TextView, input: CharSequence) {
    textView.text = input
    textView.movementMethod = LinkTouchMovementMethod.getMovementInstance()
    textView.isFocusable = false
    textView.isClickable = false
    textView.isLongClickable = false
}