package com.taskail.mixion.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.remote.Result
import com.taskail.mixion.search.SearchAdapter
import kotlinx.android.synthetic.main.fragment_mentions.*
import java.util.ArrayList


/**
 *Created by ed on 3/6/18.
 */
class MentionsFragment: Fragment(),
        ProfileContract.MentionsView, SearchAdapter.SearchAdapterCallback {

    override lateinit var presenter: ProfileContract.Presenter

    override lateinit var results: ArrayList<Result>

    lateinit var mentionsAdapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mentions, container, false)

        mentionsAdapter = SearchAdapter(results, this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        mentionsRecycler.layoutManager = layoutManager
        mentionsRecycler.itemAnimator = DefaultItemAnimator()
        mentionsRecycler.adapter = mentionsAdapter
    }

    override fun onResume() {
        super.onResume()

        presenter.getUserMentions()
    }

    override fun onItemSelected(author: String, permlink: String) {
        presenter.onMentionsItemSelected(author, permlink)
    }

    override fun setResults() {
        mentionsAdapter.notifyDataSetChanged()
    }

    override fun noResultsFound() {
        no_mentions.visibility = View.VISIBLE
    }

}