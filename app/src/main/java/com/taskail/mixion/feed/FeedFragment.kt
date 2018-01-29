package com.taskail.mixion.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion
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
class FeedFragment : Fragment(), FeedContract.View {

    val TAG = "Feed Fragment"

    interface Callback {
        fun onSearchRequested()
        fun onAccountRequested()
        fun hideBottomNav()
        fun showBottomNav()
        fun getFilterMenuAnchor(): View?
        fun onTagDialogRequested()
        fun openDiscussionRequested(discussion: SteemDiscussion)
    }

    private val filterMenuCallback = FilterMenuCallback()
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
            R.id.menu_feed_filter -> {
                val callback = getCallback() ?: return false
                callback.getFilterMenuAnchor()?.let { showFilterMenu(it) }
                true
            }
            R.id.menu_user_account -> {
                getCallback()?.onAccountRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFilterMenu(anchor: View){

        val filterMenuView = FilterMenuView(context)
        filterMenuView.show(anchor, filterMenuCallback)
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
    }

    private fun getCallback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

    private inner class FeedCallBack : FeedRVAdapter.FeedAdapterCallBack{
        override fun onDiscussionSelected(discussion: SteemDiscussion) {

            Log.d(TAG, " Feed Clicked")
            getCallback()?.openDiscussionRequested(discussion)
        }
    }

    private inner class FilterMenuCallback : FilterMenuView.Callback{
        override fun onHotSelected() {
            presenter.sortBy("Hot")
        }

        override fun onNewSelected() {
            presenter.sortBy("New")
        }

        override fun onTrendingSelected() {
            presenter.sortBy("Trending")
        }

        override fun onPromotedSelected() {
            presenter.sortBy("Promoted")
        }

        override fun onTagsSelected() {
            getCallback()?.onTagDialogRequested()
        }

    }
}