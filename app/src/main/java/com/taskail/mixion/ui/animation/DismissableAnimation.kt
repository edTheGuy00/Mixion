package com.taskail.mixion.ui.animation

/**
 *Created by ed on 3/29/18.
 */

interface DismissableAnimation {

    interface OnDismissedListener {
        fun onDismissed()
    }

    fun dismiss(listner: OnDismissedListener)
}