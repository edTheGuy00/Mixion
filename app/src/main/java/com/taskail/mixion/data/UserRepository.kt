package com.taskail.mixion.data

import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.source.remote.RemoteUserDataSource

/**
 *Created by ed on 3/3/18.
 */

class UserRepository(
        private val remoteDataSource: RemoteUserDataSource) :
        UserDataSource {

    override fun getUserBlog(user: String, callback: UserDataSource.DataLoadedCallback<SteemDiscussion>) {
        remoteDataSource.getUserBlog(user, callback)
    }

    override fun getUserMentions(user: String, callback: UserDataSource.UserMentionsCallback) {
        remoteDataSource.getUserMentions(user, callback)
    }

}