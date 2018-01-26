package com.taskail.mixion.data

import com.taskail.mixion.data.models.SteemDiscussion

/**
 *Created by ed on 1/24/18.
 */
interface SteemitDataSource {

    interface Remote{

        var tag: String

        var loadCount: Int

        fun getFeed(callback: DataLoadedCallback, sortBy: String)

        fun getMoreFeed(callback: DataLoadedCallback, sortBy: String, startAuthor: String, startPermLink: String)

    }

    interface DataLoadedCallback{

        fun onDataLoaded(steem: Array<SteemDiscussion>)

        fun onLoadError(error: Throwable)
    }

    fun getFeed(callback: DataLoadedCallback, sortBy: String)

    fun getMoreFeed(callback: DataLoadedCallback, sortBy: String, startAuthor: String, startPermLink: String)
}