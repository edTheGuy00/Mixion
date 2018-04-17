package com.taskail.mixion.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.remote.SteemDiscussion
import com.taskail.mixion.feed.FeedRVAdapter
import kotlinx.android.synthetic.main.fragment_blog.*
import java.util.ArrayList

/**
 *Created by ed on 2/23/18.
 */
class BlogFragment: Fragment(), ProfileContract.BlogView {

    override lateinit var presenter: ProfileContract.Presenter

    override lateinit var discussionFromResponse: ArrayList<SteemDiscussion>

    private lateinit var adapter: FeedRVAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blog, container, false)

        discussionFromResponse = ArrayList<SteemDiscussion>()

        adapter = FeedRVAdapter(discussionFromResponse, {
            presenter.openDiscussion(it)
        },{

        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter

        presenter.getUserBlog()
    }

    override fun showFeed() {
        adapter.notifyDataSetChanged()
    }

    override fun showMoreFeed(previousSize: Int, newSize: Int) {
        adapter.notifyItemRangeInserted(previousSize, newSize)
    }

}