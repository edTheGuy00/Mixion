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

        fun displayBtnInfo(votes: String, payout: String, user: String, timeAgo: String)

        fun displayDtube()

        fun displayYoutube(videoId: String)

        fun displayMarkdownBody(body: String, markdown: Bypass)

    }

    interface Presenter: BasePresenter{


    }
}