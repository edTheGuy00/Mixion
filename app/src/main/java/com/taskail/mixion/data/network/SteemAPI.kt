package com.taskail.mixion.data.network

import com.taskail.mixion.data.models.remote.AccountVotes
import com.taskail.mixion.data.models.remote.ContentReply
import com.taskail.mixion.data.models.remote.SteemDiscussion
import com.taskail.mixion.data.models.remote.Tags
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by ed on 4/16/18.
 */

interface SteemAPI {

    @GET("get_discussions_by_feed")
    fun getUserFeed(@Query("query") userNameLimit: String): Observable<Array<SteemDiscussion>>

    @GET("get_discussions_by_blog")
    fun getUserBlog(@Query("query") userNameLimit: String): Observable<Array<SteemDiscussion>>

    @GET("get_discussions_by_trending")
    fun getTrendingDiscussions(@Query("query") tagLimit: String): Observable<Array<SteemDiscussion>>

    @GET("get_discussions_by_hot")
    fun getHotDiscussions(@Query("query") tagLimit: String): Observable<Array<SteemDiscussion>>

    @GET("get_discussions_by_created")
    fun getNewestDiscussions(@Query("query") tagLimit: String): Observable<Array<SteemDiscussion>>

    @GET("get_discussions_by_promoted")
    fun getPromotedDiscussions(@Query("query") tagLimit: String): Observable<Array<SteemDiscussion>>

    @GET("get_content")
    fun getContent(
            @Query("author") author: String,
            @Query("permlink") permlink: String): Observable<SteemDiscussion>

    @GET("get_content_replies")
    fun getContentReplies(
            @Query("author") author: String,
            @Query("permlink") permlink: String): Observable<Array<ContentReply>>

    @GET("get_trending_tags")
    fun getTags(@Query("afterTag") afterTag: String, @Query("limit") limit: Int?): Observable<Array<Tags>>

    @GET("get_account_votes")
    fun getAccountVotes(@Query("voter") voter: String): Observable<Array<AccountVotes>>
}