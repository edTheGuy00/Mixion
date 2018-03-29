package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import com.taskail.mixion.BasePresenter
import com.taskail.mixion.BaseView
import com.taskail.mixion.data.models.ContentReply
import com.taskail.mixion.ui.animation.RevealAnimationSettings

/**
 *Created by ed on 1/28/18.
 */
interface DiscussionContract {

    interface View : BaseView<Presenter> {

        fun displayTitle(title: String)

        fun displayBtnInfo(votes: String, payout: String, user: String, timeAgo: String)

        fun displayDtube(videoImg: String?, videoUrl: String?)

        fun displayYoutube(videoId: String)

        fun displayMarkdownBody(body: String, markdown: Bypass)

        fun displayComments(commentsFromResponse: Array<ContentReply>)

        fun noComments()

        fun onBackPressed(): Boolean
    }

    interface Presenter: BasePresenter{

        fun revealReplyFragment(revealSettings: RevealAnimationSettings)
    }
}