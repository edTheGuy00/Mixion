package com.taskail.mixion.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.network.getUserProfile
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.ui.ViewPagerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.layout_user_info.*
import java.text.NumberFormat

/**
 *Created by ed on 2/23/18.
 */

class ProfileActivity : BaseActivity(), ProfileContract.Presenter{

    private val TAG = javaClass.simpleName

    override fun start() {
        steemitRepository?.remoteRepository?.getUserBlog(getUserName(), object :
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
    private lateinit var disposable: CompositeDisposable
    private lateinit var user: String

    companion object
    {
        fun myProfile(context: Context): Intent
        {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        disposable = CompositeDisposable()

        setupViewPager()
        setupUserInfo()
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

    private fun setupUserInfo(){
        disposable.add(getUserProfile(getUserName())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            setFollowerCount(it._getFollowCount()?.follower_count())
                            setFollowingCount(it._getFollowCount()?.following_count())
                            setPostCount(it.user()?.post_count())
                            user_name.text = it.user()?.profile()?.name()
                        },
                        {
                            Log.e(TAG, it.message)
                        }))
    }

    private fun setFollowerCount(count: Int?)
    {
        if (count != null)
            followers_count.text = resources.getQuantityString(R.plurals.follower_count,
                    count,
                    NumberFormat.getInstance().format(count))
    }

    private fun setFollowingCount(count: Int?)
    {
        if (count != null)
            following_count.text = resources.getQuantityString(R.plurals.following_count,
                    count,
                    NumberFormat.getInstance().format(count))
    }

    private fun setPostCount(count: Int?)
    {
        if (count != null)
            posts_count.text = resources.getQuantityString(R.plurals.post_count,
                    count,
                    NumberFormat.getInstance().format(count))
    }

}