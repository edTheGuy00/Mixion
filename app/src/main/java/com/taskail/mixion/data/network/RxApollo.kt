package com.taskail.mixion.data.network

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.taskail.mixion.GetUserDetailsQuery
import io.reactivex.Observable

/**
 *Created by ed on 3/5/18.
 */

fun getUserProfile(user: String): Observable<GetUserDetailsQuery.Data>
{

    return Observable.create { emitter ->
        userQueryCall(user).enqueue(object : ApolloCall.Callback<GetUserDetailsQuery.Data>()
        {
            override fun onFailure(e: ApolloException)
            {
                emitter.onError(e)
            }

            override fun onResponse(response: Response<GetUserDetailsQuery.Data>)
            {
                val data = response.data()
                if (data != null)
                    emitter.onNext(data)
            }

        })
    }
}