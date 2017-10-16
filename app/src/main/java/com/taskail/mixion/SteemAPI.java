package com.taskail.mixion;

import com.taskail.mixion.models.AskSteem;
import com.taskail.mixion.models.SteemDiscussion;

import io.reactivex.Observable;
import retrofit2.Call;
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
    Observable<SteemDiscussion[]> getRXTrendingDiscussions(@Query("query") String tagLimit);

    @GET("get_discussions_by_hot")
    Observable<SteemDiscussion[]> getRxHotDiscussions(@Query("query") String tagLimit);

    @GET("get_discussions_by_created")
    Observable<SteemDiscussion[]> getRxNewestDiscussions(@Query("query") String tagLimit);

    @GET("get_discussions_by_promoted")
    Observable<SteemDiscussion[]> getrxPromotedDiscussions(@Query("query") String tagLimit);

    @GET("get_content")
    Observable<SteemDiscussion> getContent(
            @Query("author") String author,
            @Query("permlink") String permlink);

}
