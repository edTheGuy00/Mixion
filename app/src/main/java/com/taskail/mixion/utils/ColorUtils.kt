@file:JvmName("ColorUtils")
package com.taskail.mixion.utils

import android.support.annotation.CheckResult
import android.support.annotation.ColorInt
import android.support.annotation.IntRange

/**
 *Created by ed on 2/23/18.
 */


/**
 * Set the alpha component of `color` to be `alpha`.
 */
@CheckResult
@ColorInt
fun modifyAlpha(@ColorInt color: Int,
                @IntRange(from = 0, to = 255) alpha: Int): Int {
    return color and 0x00ffffff or (alpha shl 24)
}