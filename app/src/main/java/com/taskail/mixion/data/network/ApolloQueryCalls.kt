package com.taskail.mixion.data.network

import com.apollographql.apollo.ApolloQueryCall
import com.taskail.mixion.GetUserDetailsQuery

/**
 *Created by ed on 3/5/18.
 */

fun userQueryCall(user: String) : ApolloQueryCall<GetUserDetailsQuery.Data> {
    return getApolloClient()
            .query(buildUserQuery(user))
}

private fun buildUserQuery(user: String): GetUserDetailsQuery {
    return GetUserDetailsQuery.builder()
            .user(user)
            .build()
}