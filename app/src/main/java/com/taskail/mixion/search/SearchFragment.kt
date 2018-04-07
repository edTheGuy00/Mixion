package com.taskail.mixion.search

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.fragment.BaseFragment
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

    val TAG = javaClass.simpleName

    override lateinit var presenter: SearchContract.Presenter

    override lateinit var results: ArrayList<Result>

    lateinit var searchAdapter: SearchAdapter

    private var fabIsVisible = true

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
                if (dy > 0 && fabIsVisible) {
                    fabIsVisible = false
                    fabSortSearch.hide()
                } else if (dy < 0 && !fabIsVisible) {
                    fabIsVisible = true
                    fabSortSearch.show()
                }
            }
        })

        fabSortSearch.setOnClickListener {
            openSortDialog()
        }
    }

    private fun openSortDialog() {
        val sortItems = context?.resources?.getStringArray(R.array.sort_search)
        AlertDialog
                .Builder(context)
                .setTitle(R.string.sort_by)
                .setSingleChoiceItems(sortItems, -1, {
                    dialogInterface, i -> handleDialogSelection(dialogInterface, i)
                })
                .create()
                .show()
    }

    private fun handleDialogSelection(dialogInterface: DialogInterface, position: Int){

        when (position) {
            0 -> presenter.sortBy("created", "desc")
            1 -> presenter.sortBy("created", "asc")
            2 -> presenter.sortBy("children", "desc")
            3 -> presenter.sortBy("net_votes", "desc")
        }
        dialogInterface.dismiss()
    }

    private inner class QueryTextChangeListener : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            if (query != null) {
                presenter.askSteem(query)
                view?.hideSoftKeyboard()
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
        hideLogo()
        searchAdapter.notifyDataSetChanged()
    }

    override fun toggleLoading(){
        if (isLoading())
            searchLoadingIndicator.visibility = View.GONE
        else
            searchLoadingIndicator.visibility = View.VISIBLE
    }

    private fun isLoading(): Boolean {
        return searchLoadingIndicator.visibility == View.VISIBLE
    }

    private fun hideLogo(){
        if (askSteemLogo.visibility != View.GONE) {
            askSteemLogo.visibility = View.GONE
        }
    }

    override fun onBackPressed() : Boolean {
        closeFragment(searchContainer)
        return true
    }

    override fun onDestroyView() {
        results.clear()
        super.onDestroyView()
    }

}