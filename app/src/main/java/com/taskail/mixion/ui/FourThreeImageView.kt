package com.taskail.mixion.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 *Created by ed on 1/27/18.
 */
class FourThreeImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val fourThreeHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 3 / 4,
                MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, fourThreeHeight)
    }
}