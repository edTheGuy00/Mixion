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

public class LockableViewPager extends ViewPager {

    public boolean swipeable;

    public LockableViewPager(Context context) {
        super(context);
    }

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeable = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.swipeable){
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.swipeable) {
            return super.onInterceptTouchEvent(ev);
        }

        return false;
    }

    public void setSwipeable(boolean swipeable){
        this.swipeable = swipeable;
    }
}

