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

    interface MainView : BaseView<Presenter> {

        fun displayToast(message: String)

        fun displayTitle(title: String)

        fun displayBtnInfo(votes: String, payout: String, user: String, timeAgo: String)

        fun displayDtube(videoImg: String?, videoUrl: String?)

        fun displayYoutube(videoId: String)

        fun displayMarkdownBody(body: String, markdown: Bypass)

        fun displayComments(commentsFromResponse: Array<ContentReply>)

        fun noComments()

        fun onBackPressed(): Boolean
    }

    interface BottomSheetView : BaseView<Presenter> {

    }

    interface ReplyView : BaseView<Presenter> {

    }

    interface Presenter: BasePresenter{

        fun revealReplyFragment(revealSettings: RevealAnimationSettings)

        fun dismissReplyFragment()

        fun openCommentThread(author: String, body: String, permlink: String)

        fun postCommentReply(author: String, permlink: String, content: String)

        fun postDiscussionreply(content: String)
    }
}