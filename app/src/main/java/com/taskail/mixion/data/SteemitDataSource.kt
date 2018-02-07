package com.taskail.mixion.data

import com.taskail.mixion.data.models.AskSteemResult
import com.taskail.mixion.data.models.ContentReply
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

        fun getUserFeed(callback: DataLoadedCallback<SteemDiscussion>)

        fun getMoreUserFeed(startAuthor: String, startPermLink: String, callback: DataLoadedCallback<SteemDiscussion>)

        fun getFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String)

        fun getMoreFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String, startAuthor: String, startPermLink: String)

        fun getTags(callback: DataLoadedCallback<Tags>)

        fun getDiscussion(callBack: DiscussionLoadedCallBack, author: String, permlink: String)

        fun getComments(author: String, permlink: String, callback: DataLoadedCallback<ContentReply>)
    }

    interface Local{

        fun getTags(callback: DataLoadedCallback<RoomTags>)

        fun saveTags(tags: RoomTags)

        fun deleteTags()
    }

    fun getDiscussion(author: String, permlink: String, callBack: DiscussionLoadedCallBack)

    fun getTags(callback: DataLoadedCallback<RoomTags>)

    fun getUserFeed(callback: DataLoadedCallback<SteemDiscussion>)

    fun getMoreUserFeed(startAuthor: String, startPermLink: String, callback: DataLoadedCallback<SteemDiscussion>)

    fun getFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String)

    fun getMoreFeed(callback: DataLoadedCallback<SteemDiscussion>, sortBy: String, startAuthor: String, startPermLink: String)

    interface DataLoadedCallback <T>{

        fun onDataLoaded(list: List<T>)

        fun onDataLoaded(array: Array<T>)

        fun onLoadError(error: Throwable)
    }

    interface DiscussionLoadedCallBack{

        fun onDataLoaded(discussion: SteemDiscussion)

        fun onLoadError(error: Throwable)
    }
}
