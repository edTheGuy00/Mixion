package com.taskail.mixion.main

import android.os.Bundle
import android.view.View
import com.taskail.mixion.R
import com.taskail.mixion.activity.SingleFragmentToolbarActivity
import com.taskail.mixion.data.source.local.MixionDatabase

/**
 *Created by ed on 1/19/18.
 *
 * The main Entry point for Mixion Application
 */

class MainActivity : SingleFragmentToolbarActivity<MainFragment>(),
        MainFragment.Callback{

    lateinit var mixionDatabase: MixionDatabase

    override fun getFilterMenuAnchor(): View? {
        return getToolbar().findViewById<View?>(R.id.menu_feed_filter)
    }

    override fun getDatabase(): MixionDatabase? {
        return mixionDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mixionDatabase = MixionDatabase.getInstance(this)
    }

    override fun onSearchOpen() {
        hideToolbar()
    }

    override fun onSearchClosed() {
        showToolbar()
    }

    override fun createFragment(): MainFragment {
        return MainFragment.newInstance()
    }

    override fun onBackPressed() {
        if (getFragment()?.onBackPressed()!!){
            return
        }
        finish()
    }
}