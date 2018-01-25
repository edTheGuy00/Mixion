@file:Suppress("UNCHECKED_CAST")

package com.taskail.mixion.utils

import android.support.annotation.NonNull
import android.support.v4.app.Fragment

/**
 *Created by ed on 1/25/18.
 */


@NonNull
fun <T> getCallback(@NonNull fragment: Fragment, @NonNull callback: Class<T>) : T? {

    if (callback.isInstance(fragment.targetFragment)) {

        return fragment.targetFragment as T
    }
    if (callback.isInstance(fragment.parentFragment)) {

        return fragment.parentFragment as T
    }
    return if (callback.isInstance(fragment.activity)) {

        fragment.activity as T
    } else null
}