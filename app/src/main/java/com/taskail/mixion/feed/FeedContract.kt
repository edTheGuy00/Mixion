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

        fun showFeedType(feed: String)

        fun showMoreFeed(previousSize: Int, newSize: Int)

        fun userHasLoggedIn()

    }

    interface Presenter : BasePresenter{

        fun userStatus(loggedIn: Boolean)

        fun sortBy(sortBy: String)

        fun getByTag(tag: String)

        fun getMyFeed()

        fun fetchMore(lastPostLocation: Int)
    }
}