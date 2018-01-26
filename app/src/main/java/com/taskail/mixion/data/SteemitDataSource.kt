package com.taskail.mixion.data

import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.models.Tags

/**
 *Created by ed on 1/24/18.
 */
interface SteemitDataSource {

    interface Remote{

        var tag: String

        var loadCount: Int

        fun getFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String)

        fun getMoreFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String, startAuthor: String, startPermLink: String)

        fun getTags(callback: DataLoadedCallback<Tags>)
    }

    interface Local{

        fun getTags()
    }


    interface DataLoadedCallback <T>{

        fun onDataLoaded(steem: Array<T>)

        fun onLoadError(error: Throwable)
    }

    fun getFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String)

    fun getMoreFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String, startAuthor: String, startPermLink: String)
}