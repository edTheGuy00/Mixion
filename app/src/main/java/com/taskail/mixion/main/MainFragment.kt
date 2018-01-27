package com.taskail.mixion.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.adapters.ViewPagerAdapter
import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.source.local.LocalDataSource
import com.taskail.mixion.data.source.local.MixionDatabase
import com.taskail.mixion.data.source.remote.RemoteDataSource
import com.taskail.mixion.data.source.remote.SteemAPI
import com.taskail.mixion.data.source.remote.getRetrofitClient
import com.taskail.mixion.dialog.TagDialog
import com.taskail.mixion.feed.FeedFragment
import com.taskail.mixion.feed.FeedPresenter
import com.taskail.mixion.utils.getCallback
import com.taskail.mixion.utils.hideBottomNavigationView
import com.taskail.mixion.utils.showBottomNavigationView
import eu.bittrade.libs.steemj.base.models.AccountName
import eu.bittrade.libs.steemj.configuration.SteemJConfig
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*

/**
 *Created by ed on 1/19/18.
 */
class MainFragment : Fragment(), FeedFragment.Callback {

    val TAG = "MainFragment"

    override fun onFeedSearchRequested() {

    }

    override fun onAccountRequested() {
        if (!getCurrentUser().isEmpty){
            //User logged In, get info, open new activity
        } else {
            //login
        }
    }

    override fun getFilterMenuAnchor(): View? {
        return getCallback()?.getFilterMenuAnchor()
    }

    interface Callback{
        fun getFilterMenuAnchor(): View?

        fun getDatabase(): MixionDatabase?
    }
    private var remoteDisposable = CompositeDisposable()
    private var localDisposable = CompositeDisposable()
    private var steemitAPI: SteemAPI? = null
    private val FEED_FRAGMENT = 0
    private lateinit var feedPresenter: FeedPresenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = ViewPagerAdapter(childFragmentManager)

        initViews(pagerAdapter)
    }

    private fun initViews(adapter: ViewPagerAdapter){

        val feedFragment = FeedFragment.getInstance()
        adapter.addFragment(feedFragment, "feed Fragment").also {
            feedPresenter = FeedPresenter(feedFragment, getRepository())
        }

        lockableViewPager.adapter = adapter
        lockableViewPager.currentItem = FEED_FRAGMENT
    }

    private fun getRepository(): SteemitRepository{
        return SteemitRepository.getInstance(createRemoteRepo(), createLocalRepo())
    }

    private fun createRemoteRepo() : RemoteDataSource {
        return RemoteDataSource.getInstance(remoteDisposable, createSteemApi())
    }

    private fun createLocalRepo(): LocalDataSource {
        return LocalDataSource.getInstance(getCallback()?.getDatabase()?.tagsDao()!!, localDisposable)
    }

    private fun createSteemApi() : SteemAPI {
        return steemitAPI ?: getRetrofitClient().create(SteemAPI::class.java).apply {
            steemitAPI = this
        }
    }

    private fun getCurrentUser() : AccountName{
        return getSteemJConfig().defaultAccount
    }

    private fun getSteemJConfig(): SteemJConfig{
        return SteemJConfig.getInstance()
    }

    override fun hideBottomNav() {
        Log.d("Main Frag", "HIde")
        bottomNavView.hideBottomNavigationView()
    }

    override fun showBottomNav() {
        Log.d("Main Frag", "show")
        bottomNavView.showBottomNavigationView()
    }

    override fun onTagDialogRequested() {
        TagDialog(context, getRepository(), object : TagDialog.TagDialogCallback{
            override fun onTagSelected(tag: String) {
                feedPresenter.getByTag(tag)
            }

        }).show()
    }


    companion object {
        @JvmStatic fun newInstance(): MainFragment{
            val fragment = MainFragment()
            fragment.retainInstance = true
            return fragment
        }
    }

    override fun onDestroy() {
        remoteDisposable.dispose()
        super.onDestroy()
    }

    private fun getCallback(): Callback? {
        return getCallback(this, Callback::class.java)
    }
}
