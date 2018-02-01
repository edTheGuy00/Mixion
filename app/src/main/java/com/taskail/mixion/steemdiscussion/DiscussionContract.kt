package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import com.taskail.mixion.BasePresenter
import com.taskail.mixion.BaseView

/**
 *Created by ed on 1/28/18.
 */
interface DiscussionContract {

    interface View : BaseView<Presenter> {

        fun displayTitle(title: String)

        fun displayHtmlBody(body: CharSequence)

        fun displayDtube()

        fun displayYoutube(videoId: String)

        fun displayMarkdownBody(body: String, markdown: Bypass)

        fun displayImages(images: List<String>)

        fun setUpVoteCount(votes: String)

        fun setPayout(payout: String)

        fun setUser(user: String)

        fun setTimeAgo(timeAgo: String)

        fun setNoImages()

        fun displaySimpleHtml(body: String)
    }

    interface Presenter: BasePresenter{


    }
}