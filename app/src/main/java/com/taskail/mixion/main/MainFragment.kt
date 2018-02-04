package com.taskail.mixion.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.taskail.mixion.BackPressedHandler
import com.taskail.mixion.R
import com.taskail.mixion.data.source.remote.AskSteemRepository
import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.source.local.LocalDataSource
import com.taskail.mixion.data.source.local.MixionDatabase
import com.taskail.mixion.data.source.remote.*
import com.taskail.mixion.dialog.TagDialog
import com.taskail.mixion.feed.FeedFragment
import com.taskail.mixion.feed.FeedPresenter
import com.taskail.mixion.search.SearchFragment
import com.taskail.mixion.search.SearchPresenter
import com.taskail.mixion.steemdiscussion.loadDiscussionIntent
import com.taskail.mixion.steemdiscussion.openDiscussionIntent
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

var steemitRepository: SteemitRepository? = null

class MainFragment : Fragment(),
        BackPressedHandler,
        FeedFragment.Callback,
        SearchFragment.Callback {

    val TAG = "MainFragment"

    private lateinit var result: Drawer

    override fun getFilterMenuAnchor(): View? {
        return getCallback()?.getFilterMenuAnchor()
    }

    interface Callback{
        fun onSearchOpen()
        fun onSearchClosed()
        fun getFilterMenuAnchor(): View?
        fun getDatabase(): MixionDatabase?
        fun getMainToolbar(): Toolbar
    }
    private var remoteDisposable = CompositeDisposable()
    private var localDisposable = CompositeDisposable()
    private var steemitAPI: SteemAPI? = null
    private var askSteemApi: AskSteemApi? = null
    private var askSteemDisposable: CompositeDisposable? = null
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

        result = drawer {
            savedInstance = savedInstanceState
            toolbar = this@MainFragment.getCallback()?.getMainToolbar()!!
            actionBarDrawerToggleAnimated = true
            rootViewRes = R.id.drawerContainer

            primaryItem(R.string.home) { iicon = FontAwesome.Icon.faw_home }
            primaryItem(R.string.profile) { iicon = FontAwesome.Icon.faw_user }
            primaryItem(R.string.settings) { iicon = FontAwesome.Icon.faw_cog }
            sectionHeader(R.string.app_name)
            secondaryItem(R.string.about) { iicon = FontAwesome.Icon.faw_info_circle }
            secondaryItem(R.string.feed_back) { iicon = FontAwesome.Icon.faw_coffee }
            secondaryItem(R.string.github) { iicon = FontAwesome.Icon.faw_github }
            secondaryItem(R.string.about) { iicon = FontAwesome.Icon.faw_bullhorn }
            onItemClick { view, position, drawerItem ->
                Log.d("frag", position.toString())
                result.closeDrawer()
                return@onItemClick true
            }
        }
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
        return steemitRepository ?: SteemitRepository.getInstance(createRemoteRepo(), createLocalRepo()).apply {
            steemitRepository = this
        }
    }

    private fun createRemoteRepo() : RemoteDataSource {
        return RemoteDataSource.getInstance(remoteDisposable, createSteemApi())
    }

    private fun createLocalRepo(): LocalDataSource {
        return LocalDataSource.getInstance(getCallback()?.getDatabase()?.tagsDao()!!, localDisposable)
    }

    private fun createSteemApi() : SteemAPI {
        return steemitAPI ?: getRetrofitClient(baseUrl).create(SteemAPI::class.java).apply {
            steemitAPI = this
        }
    }

    private fun getAskSteemRepo() : AskSteemRepository {
        return AskSteemRepository.getInstance(getAskSteemDisposable(), createAskSteemApi())
    }

    private fun getAskSteemDisposable(): CompositeDisposable{
        return askSteemDisposable ?: CompositeDisposable()
    }

    private fun createAskSteemApi() : AskSteemApi {
        return askSteemApi ?: getRetrofitClient(askSteemUrl).create(AskSteemApi::class.java).apply {
            askSteemApi = this
        }
    }

    private fun getCurrentUser() : AccountName{
        return getSteemJConfig().defaultAccount
    }

    private fun getSteemJConfig(): SteemJConfig{
        return SteemJConfig.getInstance()
    }

    override fun hideBottomNav() {
        bottomNavView.hideBottomNavigationView()
    }

    override fun showBottomNav() {
        bottomNavView.showBottomNavigationView()
    }

    override fun onTagDialogRequested() {
        TagDialog(context, getRepository(), object : TagDialog.TagDialogCallback{
            override fun onTagSelected(tag: String) {
                feedPresenter.getByTag(tag)
            }

        }).show()
    }

    private fun searchFragment(): SearchFragment? {
        return childFragmentManager.findFragmentById(R.id.fragment_main_container) as SearchFragment?
    }

    override fun onSearchRequested() {
        var fragment: Fragment? = searchFragment()
        if (fragment == null) {
            fragment = SearchFragment.newInstance().apply {
                SearchPresenter(this, getAskSteemRepo())
            }
            childFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_main_container, fragment)
                    .commitNow()
        }

        getCallback()?.onSearchOpen()

    }

    override fun onSearchClosed() {
        childFragmentManager.beginTransaction().remove(searchFragment()).commitNowAllowingStateLoss()
        getCallback()?.onSearchClosed()
    }

    override fun onSearchResultSelected(author: String, permlink: String) {
        startActivity(loadDiscussionIntent(context!!, author, permlink))
    }

    override fun openDiscussionRequested(discussion: SteemDiscussion) {
        startActivity(openDiscussionIntent(context!!, discussion))
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
        localDisposable.dispose()
        super.onDestroy()
    }

    private fun getCallback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

    override fun onBackPressed(): Boolean {
        val searchFragment = searchFragment()
        if (searchFragment != null && searchFragment.onBackPressed()) {
            return true
        }

        return false
    }
}
