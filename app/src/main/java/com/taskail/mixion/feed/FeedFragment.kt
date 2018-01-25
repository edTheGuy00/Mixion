package com.taskail.mixion.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.utils.getCallback
import com.taskail.mixion.views.FilterMenuView
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*

/**
 *Created by ed on 1/24/18.
 */
class FeedFragment : Fragment(), FeedContract.View {

    val TAG = "Feed Fragment"

    interface Callback {
        fun onFeedSearchRequested()
        fun onAccountRequested()
        fun getFilterMenuAnchor(): View?
    }

    private val filterMenuCallback = FilterMenuCallback()

    override fun showFeed() {
        adapter.notifyDataSetChanged()
    }

    override fun showMoreFeed() {
        adapter.notifyItemRangeInserted(0, 0)
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

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View=  inflater.inflate(R.layout.fragment_feed, container, false)

        adapter = FeedRVAdapter(discussionFromResponse)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_feed_search -> {
                getCallback()?.onFeedSearchRequested()
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
    }

    private fun getCallback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

    private  inner class FilterMenuCallback : FilterMenuView.Callback{
        override fun onHotSelected() {
            Log.d(TAG, "Hot selected")
        }

        override fun onNewSelected() {
            Log.d(TAG, "new selected")
        }

        override fun onTrendingSelected() {
            Log.d(TAG, "trending selected")
        }

        override fun onPromotedSelected() {
            Log.d(TAG, "promoted selected")
        }

        override fun onTagsSelected() {
            Log.d(TAG, "tags selected")
        }

    }
}