package com.taskail.mixion.feed

import com.taskail.mixion.BasePresenter
import com.taskail.mixion.BaseView

/**
 *Created by ed on 1/24/18.
 */
interface FeedContract {

    interface View : BaseView<Presenter>{

        fun showFeed()

    }

    interface Presenter : BasePresenter{

        fun loadSteemFeed()

        fun loadMoreSteem()
    }
}