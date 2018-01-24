package com.taskail.mixion.main

import android.os.Bundle
import com.taskail.mixion.activity.SingleFragmentToolbarActivity

/**
 *Created by ed on 1/19/18.
 *
 * The main Entry point for Mixion Application
 */

class MainActivity : SingleFragmentToolbarActivity<MainFragment>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun createFragment(): MainFragment {
        return MainFragment.newInstance()
    }
}