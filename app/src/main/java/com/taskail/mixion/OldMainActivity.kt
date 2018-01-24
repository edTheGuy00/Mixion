package com.taskail.mixion

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.animation.AccelerateInterpolator
import com.taskail.mixion.adapters.PagerAdapter
import com.taskail.mixion.fragments.AskSteemFragment
import com.taskail.mixion.fragments.ChatsFragment
import com.taskail.mixion.fragments.FeedFragment
import com.taskail.mixion.fragments.ProfileFragment
import com.taskail.mixion.helpers.BottomNavigationViewHelper
import com.taskail.mixion.helpers.LockableViewPager
import com.taskail.mixion.interfaces.BottomNavigationViewVisibility
import com.taskail.mixion.interfaces.FragmentLifecycle

/**Created by ed on 10/26/17.
 * Main Entry Point for Mixion
 */
class OldMainActivity : AppCompatActivity(), BottomNavigationViewVisibility{
    private var bottomNavigationView: BottomNavigationView? = null
    private var viewPager: LockableViewPager? = null
    private var pagerAdapter: PagerAdapter? = null
    private val FEED_FRAGMENT = 0
    private val ASK_STEEM_FRAGMENT = 1
    private val CHAT_FRAGMENT = 2
    private val PROFILE_FRAGMENT = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        viewPager = findViewById(R.id.container)
        pagerAdapter = PagerAdapter(supportFragmentManager)
        setupViewPager()
        startBottomNavView()
    }
    private fun setupViewPager(){
        pagerAdapter?.addFragment(FeedFragment())
        pagerAdapter?.addFragment(AskSteemFragment())
        pagerAdapter?.addFragment(ChatsFragment())
        pagerAdapter?.addFragment(ProfileFragment())

        viewPager?.adapter = pagerAdapter
        viewPager?.currentItem = FEED_FRAGMENT
        viewPager?.setSwipeable(false)

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            internal var currentPosition = FEED_FRAGMENT
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(newPosition: Int) {
                handleFragmentLifeCycle(currentPosition, newPosition)
                currentPosition = newPosition
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
    /**
     * Implements a manual lifecycle for each fragment.
     * @param currentPosition begins with OldMainActivity#FEED_FRAGMENT
     * @param newPosition updated position as the user navigates the fragments
     */
    private fun handleFragmentLifeCycle(currentPosition: Int, newPosition: Int) {
        val fragmentToHide = pagerAdapter?.getItem(currentPosition) as FragmentLifecycle
        fragmentToHide.onPauseFragment()
        val fragmentToShow = pagerAdapter?.getItem(newPosition) as FragmentLifecycle
        fragmentToShow.onResumeFragment()
    }
    private fun startBottomNavView(){
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView)
        bottomNavigationView?.setOnNavigationItemSelectedListener { item: MenuItem ->
            item.isChecked = true
            when (item.itemId) {
                R.id.feed_icon -> viewPager?.currentItem = FEED_FRAGMENT
                R.id.asksteem_icon -> viewPager?.currentItem = ASK_STEEM_FRAGMENT
                R.id.chat_icon -> viewPager?.currentItem = CHAT_FRAGMENT
                R.id.profile_icon -> viewPager?.currentItem = PROFILE_FRAGMENT
            }
            false
        }
    }
    /**
     * Hide and show the bottom navigation view
     */
    override fun hideBNV() {
        bottomNavigationView?.animate()?.translationY(bottomNavigationView!!.height.toFloat())
                ?.setInterpolator(AccelerateInterpolator(2f))?.start()
    }
    override fun showBNV() {
        bottomNavigationView?.animate()?.translationY(0f)?.interpolator = AccelerateInterpolator(2f)
    }
}