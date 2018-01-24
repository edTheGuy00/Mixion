package com.taskail.mixion.bottomNav

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.util.AttributeSet
import android.view.Menu

/**
 *Created by ed on 1/24/18.
 */
class MainBottomNavLayout : BottomNavigationView {

    constructor (context: Context) : super(context) {
        setNavViews()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        setNavViews()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        setNavViews()
    }

    private fun setNavViews(){

        for (i in 0 until MainBottomNavView.size()) {
            val navTab = MainBottomNavView.of(i)
            menu.add(Menu.NONE, i, i, navTab.text()).setIcon(navTab.icon())
        }
    }
}