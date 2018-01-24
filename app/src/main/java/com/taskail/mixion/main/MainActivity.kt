package com.taskail.mixion.main

import com.taskail.mixion.activity.SingleFragmentActivity

/**
 *Created by ed on 1/19/18.
 */

class MainActivity : SingleFragmentActivity<MainFragment>(){


    override fun createFragment(): MainFragment {
        return MainFragment.newInstance()
    }
}