package com.taskail.mixion.data.source.remote;

import com.taskail.mixion.data.models.AskSteem;
import com.taskail.mixion.data.models.ContentReply;
import com.taskail.mixion.data.models.SteemDiscussion;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**Created by ed on 10/2/17.
 */

public interface SteemAPI {

    @GET("search")
    Observable<AskSteem> searchAskSteem(@Query("q") String searchTerm);

    @GET("search")
    Observable<AskSteem> AskMoreSteem(
            @Query("q") String searchTerm,
            @Query("pg") Integer page);

    @GET("get_discussions_by_trending")
    Observable<SteemDiscussion[]> getTrendingDiscussions(@Query("query") String tagLimit);

    @GET("get_discussions_by_hot")
    Observable<SteemDiscussion[]> getHotDiscussions(@Query("query") String tagLimit);

    @GET("get_discussions_by_created")
    Observable<SteemDiscussion[]> getNewestDiscussions(@Query("query") String tagLimit);

    @GET("get_discussions_by_promoted")
    Observable<SteemDiscussion[]> getPromotedDiscussions(@Query("query") String tagLimit);

    @GET("get_content")
    Observable<SteemDiscussion> getContent(
            @Query("author") String author,
            @Query("permlink") String permlink);

    @GET("get_content_replies")
    Observable<ContentReply[]> getContentReplies(
            @Query("author") String author,
            @Query("permlink") String permlink);
}
