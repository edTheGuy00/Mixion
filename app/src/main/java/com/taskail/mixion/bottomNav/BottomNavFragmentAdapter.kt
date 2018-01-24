package com.taskail.mixion.bottomNav

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
 *Created by ed on 1/24/18.
 */
class BottomNavFragmentAdapter (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private var currentFragment: Fragment? = null

    fun getCurrentFragment(): Fragment? {
        return currentFragment
    }

    override fun getItem(position: Int): Fragment {
        return  MainBottomNavView.of(position).newInstance()
    }

    override fun getCount(): Int {
        return MainBottomNavView.size()
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        currentFragment = `object` as Fragment
        super.setPrimaryItem(container, position, `object`)
    }
}