package com.taskail.mixion.main

import android.os.Bundle
import android.view.View
import com.taskail.mixion.R
import com.taskail.mixion.activity.SingleFragmentToolbarActivity

/**
 *Created by ed on 1/19/18.
 *
 * The main Entry point for Mixion Application
 */

class MainActivity : SingleFragmentToolbarActivity<MainFragment>(), MainFragment.Callback{

    override fun getFilterMenuAnchor(): View? {
        return getToolbar().findViewById<View?>(R.id.menu_feed_filter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun createFragment(): MainFragment {
        return MainFragment.newInstance()
    }
}