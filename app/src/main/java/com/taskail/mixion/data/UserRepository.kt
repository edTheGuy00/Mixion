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

    companion object {
        private var INSTANCE: UserRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance( remoteRepository: RemoteUserDataSource):
                UserRepository{
            return INSTANCE ?: UserRepository(remoteRepository).apply {
                INSTANCE = this
            }
        }

        @JvmStatic
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}