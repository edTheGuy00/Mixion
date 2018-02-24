@file:JvmName("AnimUtils")
package com.taskail.mixion.utils

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator

/**
 *Created by ed on 2/23/18.
 */

fun getFastOutSlowInInterpolator(context: Context): Interpolator {
    return AnimationUtils.loadInterpolator(context,
            android.R.interpolator.fast_out_slow_in)
}