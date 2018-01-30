package com.taskail.mixion.data.source.remote

import com.taskail.mixion.data.models.AskSteemResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *Created by ed on 1/29/18.
 */
interface AskSteemApi {

    //call using ("search+" + term)
    @GET("search")
    fun askSteem(@Query("q") searchTerm: String): Observable<AskSteemResult>

    //call using ("search+" + term, page)
    @GET("search")
    fun askMore(
            @Query("q") searchTerm: String,
            @Query("pg") page: Int?): Observable<AskSteemResult>
}