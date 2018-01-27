package com.taskail.mixion.data

import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.models.Tags
import com.taskail.mixion.data.source.local.RoomTags

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

        fun getTags(callback: DataLoadedCallback<RoomTags>)

        fun saveTags(tags: RoomTags)
    }


    interface DataLoadedCallback <T>{

        fun onDataLoaded(list: List<T>)

        fun onDataLoaded(steem: Array<T>)

        fun onLoadError(error: Throwable)
    }

    fun <T>getTags(callback: DataLoadedCallback<T>)

    fun getFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String)

    fun getMoreFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String, startAuthor: String, startPermLink: String)
}