package com.taskail.mixion.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.taskail.mixion.utils.CIRCULAR_OUTLINE

/**
 *Created by ed on 3/8/18.
 */

class CircularImageView(context: Context,
                        attributeSet: AttributeSet):
        ImageView(context, attributeSet) {

    init {
        outlineProvider = CIRCULAR_OUTLINE
        clipToOutline = true
    }

}