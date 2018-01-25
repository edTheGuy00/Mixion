package com.taskail.mixion.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v4.widget.PopupWindowCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.taskail.mixion.R
import kotlinx.android.synthetic.main.view_filter_menu.view.*

/**
 *Created by ed on 1/25/18.
 */
class FilterMenuView(context: Context?) : FrameLayout(context) {

    init {

        View.inflate(context, R.layout.view_filter_menu, this)
    }


    private var popupWindowHost: PopupWindow? = null
    private var callback: Callback? = null

    interface Callback {
        fun onHotSelected()
        fun onNewSelected()
        fun onTrendingSelected()
        fun onPromotedSelected()
        fun onTagsSelected()
    }

    fun show(anchorView: View, callback: Callback?) {
        this.callback = callback
        popupWindowHost = PopupWindow(this, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindowHost?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindowHost?.elevation = resources.getDimension(R.dimen.filter_menu_elevation)
        }
        PopupWindowCompat.setOverlapAnchor(popupWindowHost!!, true)
        PopupWindowCompat.showAsDropDown(popupWindowHost!!, anchorView, 0, 0, Gravity.CENTER)

        initOnClickListeners()
    }

    private fun initOnClickListeners(){

        filter_new.setOnClickListener {
            popupWindowHost?.dismiss()
            callback?.onNewSelected()
        }

        filter_hot.setOnClickListener {
            popupWindowHost?.dismiss()
            callback?.onHotSelected()
        }

        filter_promoted.setOnClickListener {
            popupWindowHost?.dismiss()
            callback?.onPromotedSelected()
        }

        filter_trending.setOnClickListener {
            popupWindowHost?.dismiss()
            callback?.onTrendingSelected()
        }

        filter_tags.setOnClickListener {
            popupWindowHost?.dismiss()
            callback?.onTagsSelected()
        }

    }
}