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
import co.zsmb.materialdrawerkt.draweritems.badgeable.PrimaryDrawerItemKt
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import co.zsmb.materialdrawerkt.draweritems.expandable.expandableItem
import co.zsmb.materialdrawerkt.draweritems.sectionHeader
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.taskail.mixion.*
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.profile.User
import com.taskail.mixion.utils.EndlessRecyclerViewScrollListener
import com.taskail.mixion.utils.getCallback
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*

/**
 *Created by ed on 1/24/18.
 *
 * Feed fragment is responsible for all views in the feed page.
 */
class FeedFragment : Fragment(),
        FeedContract.View {

    val TAG = javaClass.simpleName

    private lateinit var result: Drawer

    interface Callback {
        fun onSearchRequested()
        fun onTagDialogRequested()
        fun openDiscussionRequested(discussion: SteemDiscussion)
        fun getDrawerToolbar(): Toolbar?
        fun getDrawerContainer(): Int
        fun onAccountRequested()
        fun setToolbarTitle(title: String)
        fun logoutUser()
        fun requestToAddNewPost()
    }

    override fun showFeed() {
        feed_loading_indicator.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }

    override fun clearItemsForNewFeed() {
        feed_loading_indicator.visibility = View.VISIBLE
        adapter.notifyDataSetChanged()
    }

    override fun showMoreFeed(previousSize: Int, newSize: Int) {
        adapter.notifyItemRangeInserted(previousSize, newSize)
    }

    override fun showFeedType(feed: String) {
        getCallback()?.setToolbarTitle(title = feed)
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

    private var fabIsVisible = true

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View=  inflater.inflate(R.layout.fragment_feed, container, false)

        discussionFromResponse = ArrayList<SteemDiscussion>()

        adapter = FeedRVAdapter(discussionFromResponse, {
            getCallback()?.openDiscussionRequested(it)
        })

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
                if (dy > 0 && fabIsVisible) {
                    fabIsVisible = false
                    fab.hide()
                } else if (dy < 0 && !fabIsVisible) {
                    fabIsVisible = true
                    fab.show()
                }
            }

        })

        fab.setOnClickListener {
            getCallback()?.requestToAddNewPost()
        }

        prepareDrawerNavigation(savedInstanceState)
    }

    private fun prepareDrawerNavigation(savedInstanceState: Bundle?){

        result = drawer {
            savedInstance = savedInstanceState
            toolbar = this@FeedFragment.getCallback()?.getDrawerToolbar()!!
            actionBarDrawerToggleAnimated = true
            rootViewRes = R.id.drawerContainer

            primaryItem(R.string.home) {
                iicon = FontAwesome.Icon.faw_home
                onClick(sortFeed(getString(R.string.myFeed)))
            }
            secondaryItem(R.string.filter_new) {
                iicon = FontAwesome.Icon.faw_star
                onClick(sortFeed(getString(R.string.filter_new)))
            }
            secondaryItem(R.string.filter_hot) {
                iicon = FontAwesome.Icon.faw_fire
                onClick(sortFeed(getString(R.string.filter_hot)))
            }
            secondaryItem(R.string.filter_trending) {
                iicon = FontAwesome.Icon.faw_font_awesome
                onClick(sortFeed(getString(R.string.filter_trending)))
            }
            secondaryItem(R.string.filter_promoted) {
                iicon = FontAwesome.Icon.faw_money
                onClick(sortFeed(getString(R.string.filter_promoted)))
            }

            sectionHeader(R.string.quick_links)
            secondaryItem(R.string.d_tube) {
                iicon = FontAwesome.Icon.faw_play_circle
                onClick(quickLinks(DTBUE))
            }
            secondaryItem(R.string.d_mania) {
                iicon = FontAwesome.Icon.faw_smile_o
                onClick(quickLinks(DMANIA))
            }

            primaryItem(R.string.browse_tags) {
                selectable = false
                onClick(miscItemsClicked(TAG_DIALOG))
            }
            sectionHeader(R.string.app_name)
            secondaryItem(R.string.about) {
                iicon = FontAwesome.Icon.faw_info_circle
                selectable = false
                onClick(miscItemsClicked(ABOUT_PAGE))
            }
            secondaryItem(R.string.donate) {
                iicon = FontAwesome.Icon.faw_money
                selectable = false
                onClick(miscItemsClicked(DONATE))
            }
            if (User.userIsLoggedIn){
                footer {
                    primaryItem (User.getUserName()!!, getString(R.string.logout)) {
                        selectable = false
                        onClick(logoutUser())

                    }
                }
            }
        }
    }

    private fun sortFeed(sort: String): (View?) -> Boolean = {
        Log.i(TAG, "sorting feed by $sort" )
        when (sort){
            getString(R.string.myFeed) -> presenter.getMyFeed()
            else -> presenter.sortBy(sort)
        }
        false
    }

    private fun quickLinks(get: Int): (View?) -> Boolean = {
        when (get) {
            DTBUE -> presenter.getDtube()
            DMANIA -> presenter.getDmania()
        }

        false
    }

    private fun miscItemsClicked(item: Int): (View?) -> Boolean = {
        when (item){
            TAG_DIALOG -> getCallback()?.onTagDialogRequested()
            ABOUT_PAGE -> Log.d(TAG, "About clicked")
            DONATE -> Log.d(TAG, "Donation Clicked")
        }
        false
    }

    private fun logoutUser(): (View?) -> Boolean = {
        result.removeAllStickyFooterItems()
        getCallback()?.logoutUser()
        false
    }

    /**
     * this Will only be called when the user logs in.
     */
    override fun userHasLoggedIn() {
        val drawerItem: IDrawerItem<*, *>
        drawerItem = PrimaryDrawerItemKt()
                .primaryItem (User.getUserName()!!, getString(R.string.logout)){
                    selectable = false
                    onClick(logoutUser())
                }
        result.addStickyFooterItem(drawerItem)
    }

    private fun getCallback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

}