package com.taskail.mixion.data

import com.taskail.mixion.data.models.AskSteemResult
import com.taskail.mixion.data.models.SteemDiscussion

/**
 *Created by ed on 3/3/18.
 */

interface UserDataSource {

    fun getUserBlog(user: String, callback: DataLoadedCallback<SteemDiscussion>)

    fun getUserMentions(user: String, callback: UserMentionsCallback)

    interface Remote
    {
        var loadCount: Int

        fun getUserBlog(user: String, callback: DataLoadedCallback<SteemDiscussion>)

        fun getUserMentions(user: String, callback: UserMentionsCallback)
    }

    interface Local
    {
        fun getUserBlog()
    }

    interface DataLoadedCallback <T>{

        fun onDataLoaded(array: Array<T>)

        fun onLoadError(error: Throwable)
    }

    interface UserMentionsCallback{

        fun onDataLoaded(askSteemResult: AskSteemResult)

        fun onLoadError(error: Throwable)
    }
}