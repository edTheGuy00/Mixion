package com.taskail.mixion.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.BackPressedHandler
import com.taskail.mixion.R
import com.taskail.mixion.data.models.Result
import com.taskail.mixion.utils.*
import kotlinx.android.synthetic.main.fragment_search.*

/**
 *Created by ed on 1/29/18.
 */
class SearchFragment : Fragment(), BackPressedHandler, SearchContract.View {

    override lateinit var presenter: SearchContract.Presenter

    override lateinit var results: List<Result>

    lateinit var searchAdapter: SearchAdapter

    companion object {
        @JvmStatic fun newInstance(): SearchFragment{
            return SearchFragment()
        }
    }

    interface Callback{
        fun onSearchClosed()
        fun onSearchResultSelected()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchAdapter = SearchAdapter(results)

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

    override fun setResults() {
        searchAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() : Boolean {
        closeSearchFragment()
        return true
    }

    private fun closeSearchFragment(){
        searchContainer.fadeOutAnimation(object : FadeOutCallBack{
            override fun onAnimationEnd() {
                val callback = callback()
                callback?.onSearchClosed()
            }
        })

        view?.hideSoftKeyboard()
    }

    private fun callback(): Callback? {
        return getCallback(this, Callback::class.java)
    }

}