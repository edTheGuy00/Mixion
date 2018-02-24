package com.taskail.mixion.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.ui.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_profile.*

/**
 *Created by ed on 2/23/18.
 */

class ProfileActivity : BaseActivity(), ProfileContract.Presenter{
    override fun start() {
        steemitRepository?.remoteRepository?.getUserBlog(User.getUserName()!!, object :
                SteemitDataSource.DataLoadedCallback<SteemDiscussion>{
            override fun onDataLoaded(list: List<SteemDiscussion>) {

            }

            override fun onDataLoaded(array: Array<SteemDiscussion>) {
                blogView.discussionFromResponse.addAll(array)
                blogView.showFeed()
            }

            override fun onLoadError(error: Throwable) {
            }

        })
    }

    override fun openDiscussion(discussion: SteemDiscussion) {

    }

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var blogView: ProfileContract.BlogView

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        setupViewPager()
    }

    private fun setupViewPager() {

        val blogFragment = BlogFragment()
        blogView = blogFragment
        viewPagerAdapter.addFragment(blogFragment, "Blog").also {
            blogView.presenter = this
        }

        profileViewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(profileViewPager)

    }

}