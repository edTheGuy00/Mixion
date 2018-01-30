package com.taskail.mixion.search

import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.models.Result
import java.util.ArrayList

/**
 *Created by ed on 1/29/18.
 */
class SearchPresenter (
        private val searchView: SearchContract.View,
        private val repository: SteemitRepository
) : SearchContract.Presenter {

    var results = ArrayList<Result>()

    init {
        searchView.presenter = this
        searchView.results = results
    }

    override fun start() {

    }

    override fun askSteem(query: String) {

    }

    override fun askMore() {

    }
}