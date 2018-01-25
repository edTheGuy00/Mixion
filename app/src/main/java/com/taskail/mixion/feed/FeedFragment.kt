package com.taskail.mixion.feed

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.*

/**
 *Created by ed on 1/24/18.
 */
class FeedFragment : Fragment(), FeedContract.View {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }
}