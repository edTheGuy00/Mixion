package com.taskail.mixion.search

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.BaseFragment
import com.taskail.mixion.R
import com.taskail.mixion.data.models.Result
import com.taskail.mixion.utils.*
import kotlinx.android.synthetic.main.fragment_search.*

/**
 *Created by ed on 1/29/18.
 */

class SearchFragment : BaseFragment(),
        SearchContract.View,
        SearchAdapter.SearchAdapterCallback {

    override lateinit var presenter: SearchContract.Presenter

    override lateinit var results: ArrayList<Result>

    lateinit var searchAdapter: SearchAdapter

    companion object {
        @JvmStatic fun newInstance(): SearchFragment{
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchAdapter = SearchAdapter(results, this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        searchView.isIconified = false
        searchContainer.fadeInAnimation()
        searchView.setOnQueryTextListener(QueryTextChangeListener())

        val layoutManager = LinearLayoutManager(context)
        searchRecyclerView.layoutManager = layoutManager
        searchRecyclerView.itemAnimator = DefaultItemAnimator()
        searchRecyclerView.adapter = searchAdapter
        searchRecyclerView.addOnScrollListener( object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, recyclerView: RecyclerView?) {
                presenter.askMore()
            }

            override fun scrollAction(dx: Int, dy: Int) {

            }
        })
    }

    private inner class QueryTextChangeListener : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
                presenter.askSteem(query)
                view?.hideSoftKeyboard()

                //TODO - set a loading indicator
            }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return true
        }

    }

    override fun onItemSelected(author: String, permlink: String) {
        searchItemSelected(author, permlink)
    }

    override fun noResultsFound() {
        //TODO - add no results message
    }

    override fun cleanResults() {
        searchAdapter.notifyDataSetChanged()
    }

    override fun setResults() {
//        hideLogo()
        searchAdapter.notifyDataSetChanged()
    }

//    private fun hideLogo(){
//        if (askSteemLogo.visibility != View.GONE) {
//            askSteemLogo.visibility = View.GONE
//        }
//    }

    override fun onBackPressed() : Boolean {
        closeFragment(searchContainer)
        return true
    }

    override fun onDestroyView() {
        results.clear()
        super.onDestroyView()
    }

}