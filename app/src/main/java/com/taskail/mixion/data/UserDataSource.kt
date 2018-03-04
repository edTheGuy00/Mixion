package com.taskail.mixion.data

import com.taskail.mixion.data.models.SteemDiscussion

/**
 *Created by ed on 3/3/18.
 */

interface UserDataSource {

    interface Remote
    {
        fun getUserBlog(user: String, callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>)
    }

    interface Local
    {
        fun getUserBlog()
    }


}