package com.taskail.mixion.profile

import android.content.res.Resources
import android.widget.TextView
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

        fun setWallet()

        fun setSteemBalance(balance: String?)

        fun setSteemDollars(dollars: String?)

        fun setSavingsBalance(balance: String?)
    }

    interface MentionsView: BaseView<Presenter>{

    }

    interface UserInfoView{

        fun setFollowerCount(textView: TextView, count: Int?)

        fun setFollowingCount(textView: TextView, count: Int?)

        fun setPostCount(textView: TextView, count: Int?)

        fun setBio(textView: TextView, about: String?)

        fun setUserName(textView: TextView, name: String?, userName: String)
    }

    interface Presenter {

        fun getUserBlog()

        fun openDiscussion(discussion: SteemDiscussion)
    }
}