package com.taskail.mixion.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.builders.footer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.profile.User
import com.taskail.mixion.utils.EndlessRecyclerViewScrollListener
import com.taskail.mixion.utils.getCallback
import com.taskail.mixion.ui.FilterMenuView
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*

/**
 *Created by ed on 1/24/18.
 *
 * Feed fragment is responsible for all views in the feed page.
 */
class FeedFragment : Fragment(),
        FeedContract.View {

    val TAG = "Feed Fragment"

    private lateinit var result: Drawer

    interface Callback {
        fun onSearchRequested()
        fun hideBottomNav()
        fun showBottomNav()
        fun onTagDialogRequested()
        fun openDiscussionRequested(discussion: SteemDiscussion)
        fun getDrawerToolbar(): Toolbar?
        fun getDrawerContainer(): Int
        fun onAccountRequested()
        fun logoutUser()
    }

    private val feedCallBack = FeedCallBack()

    override fun showFeed() {
        adapter.notifyDataSetChanged()
    }

    override fun clearItems() {
        adapter.notifyDataSetChanged()
    }

    override fun showMoreFeed(previousSize: Int, newSize: Int) {
        adapter.notifyItemRangeInserted(previousSize, newSize)
    }

    override fun showFeedType(feed: String) {
        getCallback()?.getDrawerToolbar()?.title = feed
    }

    companion object {
        var INSTANCE : FeedFragment? = null

        @JvmStatic fun getInstance() : FeedFragment{
            return INSTANCE ?: FeedFragment()
                    .also {
                        it.retainInstance = true
                    }
                    .apply {
                        INSTANCE = this
                    }
        }
    }

    override lateinit var discussionFromResponse: ArrayList<SteemDiscussion>

    override lateinit var  presenter: FeedContract.Presenter

    private lateinit var adapter: FeedRVAdapter

    private var bottomNavIsVisible = true

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View=  inflater.inflate(R.layout.fragment_feed, container, false)

        adapter = FeedRVAdapter(discussionFromResponse, feedCallBack)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_feed_search -> {
                getCallback()?.onSearchRequested()
                true
            }
            R.id.menu_user_profile -> {
                val callback = getCallback() ?: return false
                callback.onAccountRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_feed, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, recyclerView: RecyclerView?) {
                presenter.fetchMore(totalItemsCount)
            }

            override fun scrollAction(dx: Int, dy: Int) {
                if (dy > 0 && bottomNavIsVisible) {
                    bottomNavIsVisible = false
                    //getCallback()?.hideBottomNav()
                } else if (dy < 0 && !bottomNavIsVisible) {
                    bottomNavIsVisible = true
                    //getCallback()?.showBottomNav()
                }
            }

        })

        result = drawer {
            savedInstance = savedInstanceState
            toolbar = this@FeedFragment.getCallback()?.getDrawerToolbar()!!
            actionBarDrawerToggleAnimated = true
            rootViewRes = R.id.drawerContainer

            primaryItem(R.string.home) { iicon = FontAwesome.Icon.faw_home }
            primaryItem(R.string.filter_new) { iicon = FontAwesome.Icon.faw_sticky_note }
            primaryItem(R.string.filter_hot) { iicon = FontAwesome.Icon.faw_fire }
            primaryItem(R.string.filter_trending) { iicon = FontAwesome.Icon.faw_font_awesome }
            primaryItem(R.string.filter_promoted) { iicon = FontAwesome.Icon.faw_money }
            primaryItem(R.string.browse_tags)
            sectionHeader(R.string.app_name)
            secondaryItem(R.string.about) { iicon = FontAwesome.Icon.faw_info_circle }
            secondaryItem(R.string.feed_back) { iicon = FontAwesome.Icon.faw_sticky_note }
            secondaryItem(R.string.github) { iicon = FontAwesome.Icon.faw_github }
            if (User.userIsLoggedIn){
                footer {
                    primaryItem (User.getUserName()!!, getString(R.string.logout))
                }
            }

            onItemClick { view, position, drawerItem ->
                result.closeDrawer()
                handleDrawerClicks(position, drawerItem)
                return@onItemClick true
            }
        }
    }

    private fun getCallback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

    private inner class FeedCallBack : FeedRVAdapter.FeedAdapterCallBack{
        override fun onDiscussionSelected(discussion: SteemDiscussion) {
            getCallback()?.openDiscussionRequested(discussion)
        }
    }

    private fun handleDrawerClicks(position: Int, drawerItem: IDrawerItem<*, *>){
        when (position){
            1 -> presenter.sortBy("New")
            2 -> presenter.sortBy("Hot")
            3 -> presenter.sortBy("Trending")
            4 -> presenter.sortBy("Promoted")
            5 -> getCallback()?.onTagDialogRequested()
            -1 -> {
                result.removeAllStickyFooterItems()
                getCallback()?.logoutUser()
            }
        }
    }
}