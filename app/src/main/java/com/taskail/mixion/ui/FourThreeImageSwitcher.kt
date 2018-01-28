package com.taskail.mixion.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageSwitcher

/**
 *Created by ed on 1/27/18.
 */
class FourThreeImageSwitcher(context: Context, attrs: AttributeSet) : ImageSwitcher(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fourThreeHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 3 / 4,
                MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, fourThreeHeight)
    }
}