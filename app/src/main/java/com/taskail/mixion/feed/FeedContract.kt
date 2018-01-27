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

        fun clearItems()

        fun showMoreFeed(previousSize: Int, newSize: Int)

    }

    interface Presenter : BasePresenter{

        fun sortBy(sortBy: String)

        fun getByTag(tag: String)

        fun fetchMore(lastPostLocation: Int)
    }
}