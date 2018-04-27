package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import com.taskail.mixion.BasePresenter
import com.taskail.mixion.BaseView
import com.taskail.mixion.data.models.local.UserVotes
import com.taskail.mixion.data.models.remote.ContentReply
import com.taskail.mixion.ui.animation.RevealAnimationSettings

/**
 *Created by ed on 1/28/18.
 */
interface DiscussionContract {

    interface MainView : BaseView<Presenter> {

        fun setToVote(author: String, permLink: String)

        fun setToUnVote(author: String, permLink: String)

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

        fun displayComments(commentsFromResponse: Array<ContentReply>)

    }

    interface ReplyView : BaseView<Presenter> {

    }

    interface Presenter: BasePresenter{

        fun saveVote(authorPerm: String)

        fun revealReplyFragment(revealSettings: RevealAnimationSettings)

        fun dismissReplyFragment()

        fun openCommentThread(author: String, body: String, permlink: String, hasReplies: Boolean)

        fun postCommentReply(author: String, permlink: String, content: String)

        fun postDiscussionreply(content: String)

    }
}