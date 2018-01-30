package com.taskail.mixion.search

import android.util.Log
import com.taskail.mixion.data.source.remote.AskSteemData
import com.taskail.mixion.data.source.remote.AskSteemRepository
import com.taskail.mixion.data.models.AskSteemResult
import com.taskail.mixion.data.models.Result
import java.util.ArrayList

/**
 *Created by ed on 1/29/18.
 */
class SearchPresenter (
        private val searchView: SearchContract.View,
        private val repository: AskSteemRepository
) : SearchContract.Presenter {

    var results = ArrayList<Result>()

    private var hasMore = false
    private var currentQuery = ""
    private var currentPage = 0

    init {
        searchView.presenter = this
        searchView.results = results
    }

    override fun start() {

    }

    override fun askSteem(query: String) {
        if (query != currentQuery) {
            currentQuery = query
            handleSearchRequest()
        }
    }

    private fun handleSearchRequest(){

        if (isFirstQuery()){
            performSearch()
        } else {

            searchView.results.clear()
            searchView.cleanResults()

            performSearch()
        }

    }

    private fun isFirstQuery(): Boolean{
        return currentPage == 0
    }

    private fun performSearch(){

        repository.askSteem(currentQuery, object : AskSteemData.AskSteemCallback {
            override fun onDataLoaded(askSteemResult: AskSteemResult) {
                handleResults(askSteemResult)
            }

            override fun onLoadError(error: Throwable) {

            }
        })

    }
    private fun handleResults(askSteemResult: AskSteemResult){
        if (askSteemResult.results.size > 0) {

            searchView.results.addAll(askSteemResult.results)
            searchView.setResults()
        } else if (askSteemResult.hits == 0){

            searchView.noResultsFound()
        }

        hasMore = askSteemResult.pages.hasNext
        currentPage = askSteemResult.pages.current

        Log.d("page", currentPage.toString())
    }

    private fun nextPage(): Int{
        return currentPage.plus(1)
    }

    override fun askMore() {
        if (hasMore){
            repository.askMore(currentQuery, nextPage(), object : AskSteemData.AskSteemCallback {
                override fun onDataLoaded(askSteemResult: AskSteemResult) {
                    handleResults(askSteemResult)
                }

                override fun onLoadError(error: Throwable) {

                }
            })

        }
    }
}