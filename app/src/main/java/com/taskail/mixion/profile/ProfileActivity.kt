package com.taskail.mixion.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.taskail.mixion.GetUserDetailsQuery
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.data.models.Result
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.network.getUserProfile
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.steemdiscussion.loadDiscussionIntent
import com.taskail.mixion.steemdiscussion.openDiscussionIntent
import com.taskail.mixion.ui.ViewPagerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.layout_user_info.*
import java.util.ArrayList

/**
 *Created by ed on 2/23/18.
 */

class ProfileActivity : BaseActivity(), ProfileContract.Presenter{

    private val TAG = javaClass.simpleName

    override fun getUserBlog() {

        steemitRepository?.getUserBlog(getUserName(), {
            blogView.discussionFromResponse.addAll(it)
            blogView.showFeed()
        }, {
            Log.e(TAG, it.message)
        })

    }

    override fun getUserMentions() {

        if (mentionsView.results.isEmpty()) {
            steemitRepository?.getUserMentions(getUserName(), {

                if (it.results.size > 0) {

                    mentionsView.results.addAll(it.results)
                    mentionsView.setResults()
                } else if (it.hits == 0) {

                    mentionsView.noResultsFound()
                }
            }, {

            })
        }
    }

    override fun onMentionsItemSelected(author: String, permlink: String) {
        startActivity(loadDiscussionIntent(this, author, permlink))
    }

    override fun openDiscussion(discussion: SteemDiscussion) {
        startActivity(openDiscussionIntent(this, discussion))
    }

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var blogView: ProfileContract.BlogView
    private lateinit var walletView: ProfileContract.WalletView
    private lateinit var mentionsView: ProfileContract.MentionsView
    private lateinit var disposable: CompositeDisposable
    private lateinit var infoView: ProfileContract.UserInfoView
    var results = ArrayList<Result>()
//    private lateinit var user: String

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
        setSupportActionBar(profileToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        disposable = CompositeDisposable()

        infoView = UserInfoView(resources)

        setupViewPager()
        loadUserInfo()
    }

    private fun setupViewPager() {

        val blogFragment = BlogFragment()
        blogView = blogFragment
        viewPagerAdapter.addFragment(blogFragment, "Blog").also {
            blogView.presenter = this
        }
        val walletFragment = WalletFragment()
        walletView = walletFragment
        viewPagerAdapter.addFragment(walletFragment, "Wallet").also {
            walletView.presenter = this
        }
        val mentionsFragment = MentionsFragment()
        mentionsView = mentionsFragment
        viewPagerAdapter.addFragment(mentionsFragment, "Mentions").also {
            mentionsView.presenter = this
            mentionsView.results = results
        }


        profileViewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(profileViewPager)

    }

    private fun loadUserInfo(){
        disposable.add(getUserProfile(getUserName())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            updateUi(it)
                        },
                        {
                            Log.e(TAG, it.message)
                        }))
    }

    private fun updateUi(data: GetUserDetailsQuery.Data) {

        with(data) {
            with(infoView){
                setFollowerCount(followers_count, _getFollowCount()?.follower_count())
                setFollowingCount(following_count, _getFollowCount()?.following_count())
                setPostCount(posts_count, user()?.post_count())
                setBio(user_about, user()?.profile()?.about())
                setUserName(user_name, user()?.profile()?.name(), getUserName())
                setProfileImage(user()?.profile()?.profile_image(), avatar)
            }


            walletView.setSteemBalance(user()?.balance())
            walletView.setSteemDollars(user()?.sbd_balance())
            walletView.setSavingsBalance(user()?.savings_balance())
            walletView.setWallet()
        }
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

}