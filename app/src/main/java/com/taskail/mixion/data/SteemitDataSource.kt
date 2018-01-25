package com.taskail.mixion.data

import com.taskail.mixion.data.models.SteemDiscussion

/**
 *Created by ed on 1/24/18.
 */
interface SteemitDataSource {

    interface DataLoadedCallback{

        fun onDataLoaded(steem: Array<SteemDiscussion>)

        fun onLoadError(error: Throwable)

        fun onComplete()
    }

    var tag: String

    var loadCount: Int

    fun getFeed(callback: DataLoadedCallback)

    fun getNew(callback: DataLoadedCallback)

    fun getTrending(callback: DataLoadedCallback)

    fun getHot(callback: DataLoadedCallback)

    fun getPromoted(callback: DataLoadedCallback)
}