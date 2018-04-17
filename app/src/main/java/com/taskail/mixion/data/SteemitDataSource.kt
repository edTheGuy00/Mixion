package com.taskail.mixion.data

import com.taskail.mixion.data.models.*
import com.taskail.mixion.data.source.local.Drafts
import com.taskail.mixion.data.source.local.RoomTags

/**
 *Created by ed on 1/24/18.
 */
interface SteemitDataSource {

    interface Remote{

        var tag: String

        var loadCount: Int

        fun getUserFeed(response: (Array<SteemDiscussion>) -> Unit,
                        error: (Throwable) -> Unit)

        fun getUserBlog(user: String,
                        response: (Array<SteemDiscussion>) -> Unit,
                        error: (Throwable) -> Unit)

        fun getUserMentions(user: String,
                            response: (AskSteemResult) -> Unit,
                            error: (Throwable) -> Unit)

        fun getMoreUserFeed(startAuthor: String,
                            startPermLink: String,
                            response: (Array<SteemDiscussion>) -> Unit,
                            error: (Throwable) -> Unit)

        fun getFeed(sortBy: String,
                    response: (Array<SteemDiscussion>) -> Unit,
                    error: (Throwable) -> Unit)

        fun getMoreFeed(sortBy: String,
                        startAuthor: String,
                        startPermLink: String,
                        response: (Array<SteemDiscussion>) -> Unit,
                        error: (Throwable) -> Unit)

        fun getTags(response: (Array<Tags>) -> Unit,
                    error: (Throwable) -> Unit)

        fun getDiscussion(callBack: DiscussionLoadedCallBack, author: String, permlink: String)

        fun getComments(author: String, permlink: String, callback: DataLoadedCallback<ContentReply>)

        fun getAccountVotes(user: String,
                            response: (Array<AccountVotes>) -> Unit,
                            error: (Throwable) -> Unit)
    }

    interface Local{

        fun getTags(response: (List<RoomTags>) -> Unit,
                    error: (Throwable) -> Unit)

        fun saveTags(tags: RoomTags)

        fun deleteTags()

        fun getDrafts(callback: DataLoadedCallback<Drafts>)

        fun saveDraft(draft: Drafts)

        fun updateDraft(draft: Drafts)

        fun deleteDraft(id: String)
    }

    fun getUserMentions(user: String,
                        response: (AskSteemResult) -> Unit,
                        error: (Throwable) -> Unit)

    fun getUserBlog(user: String,
                    response: (Array<SteemDiscussion>) -> Unit,
                    error: (Throwable) -> Unit)

    fun getDiscussion(author: String, permlink: String, callBack: DiscussionLoadedCallBack)

    fun getTags(response: (List<RoomTags>) -> Unit,
                error: (Throwable) -> Unit)

    fun getUserFeed(response: (Array<SteemDiscussion>) -> Unit,
                    error: (Throwable) -> Unit)

    fun getMoreUserFeed(startAuthor: String,
                        startPermLink: String,
                        response: (Array<SteemDiscussion>) -> Unit,
                        error: (Throwable) -> Unit)

    fun getFeed(sortBy: String,
                response: (Array<SteemDiscussion>) -> Unit,
                error: (Throwable) -> Unit)

    fun getMoreFeed(sortBy: String,
                    startAuthor: String,
                    startPermLink: String,
                    response: (Array<SteemDiscussion>) -> Unit,
                    error: (Throwable) -> Unit)

    interface DataLoadedCallback<T>{

        fun onDataLoaded(list: List<T>)

        fun onDataLoaded(array: Array<T>)

        fun onLoadError(error: Throwable)
    }

    interface DiscussionLoadedCallBack{

        fun onDataLoaded(discussion: SteemDiscussion)

        fun onLoadError(error: Throwable)
    }
}
