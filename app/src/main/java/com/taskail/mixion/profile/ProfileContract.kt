package com.taskail.mixion.profile

import com.taskail.mixion.BaseView
import com.taskail.mixion.data.models.SteemDiscussion
import java.util.ArrayList

/**
 *Created by ed on 2/23/18.
 */

interface ProfileContract {

    interface BlogView: BaseView<Presenter> {

        var discussionFromResponse: ArrayList<SteemDiscussion>

        fun showFeed()

        fun showMoreFeed(previousSize: Int, newSize: Int)
    }

    interface WalletView: BaseView<Presenter>{

    }

    interface MentionsView: BaseView<Presenter>{

    }

    interface Presenter {

        fun getUserBlog()

        fun openDiscussion(discussion: SteemDiscussion)
    }
}