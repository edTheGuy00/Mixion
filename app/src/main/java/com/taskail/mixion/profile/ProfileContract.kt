package com.taskail.mixion.profile

import com.taskail.mixion.BasePresenter
import com.taskail.mixion.BaseView

/**
 *Created by ed on 2/23/18.
 */

interface ProfileContract {

    interface BlogView: BaseView<Presenter> {


    }

    interface Presenter : BasePresenter {

        fun openDiscussion()
    }
}