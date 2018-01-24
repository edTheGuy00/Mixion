package com.taskail.mixion.helpers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**Created by ed on 10/14/17.
 *
 * Lock the viewpager to disable swipe actions.
 * navigating to different Fragments is only done through
 * BottomNavigationView
 */

class LockableViewPager(context: Context, attributeSet: AttributeSet) : ViewPager(context, attributeSet){

        override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
        }
}

