package com.taskail.mixion.feed

import com.taskail.mixion.BasePresenter
import com.taskail.mixion.BaseView
import com.taskail.mixion.data.models.SteemDiscussion
import java.util.ArrayList

/**
 *Created by ed on 1/24/18.
 */
interface FeedContract {

    interface View : BaseView<Presenter>{

        var discussionFromResponse: ArrayList<SteemDiscussion>

        fun showFeed()

        fun showMoreFeed()

    }

    interface Presenter : BasePresenter{

        fun loadSteemFeed(steem : Array<SteemDiscussion>)

        fun loadMoreSteem(steem : Array<SteemDiscussion>)
    }
}