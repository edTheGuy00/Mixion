package com.taskail.mixion.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 *Created by ed on 1/27/18.
 */
class FourThreeView(context: Context, attrs: AttributeSet) :
        View(context, attrs) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val fourThreeHeight = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthSpec) * 3 / 4,
                View.MeasureSpec.EXACTLY)
        super.onMeasure(widthSpec, fourThreeHeight)
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }
}