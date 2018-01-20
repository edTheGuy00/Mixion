package com.taskail.mixion.data.network;

import com.apollographql.apollo.ApolloClient;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**Created by ed on 10/23/17.
 */

public class MixionApolloClient {
    private static final String TAG = "MixionApolloClient";

    private static final String BASE_URL = "https://steemql.herokuapp.com/graphql";
    private static ApolloClient apolloClient;

    public static ApolloClient getApolloCleint(){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .build();

        return apolloClient;
    }
}
