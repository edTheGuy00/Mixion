package com.taskail.mixion.search

import com.taskail.mixion.BasePresenter
import com.taskail.mixion.BaseView
import com.taskail.mixion.data.models.Result

/**
 *Created by ed on 1/29/18.
 */
interface SearchContract {

    interface View : BaseView<Presenter>{

        var results: List<Result>

        fun setResults()

    }

    interface Presenter : BasePresenter{

        fun askSteem(query: String)

        fun askMore()

    }
}