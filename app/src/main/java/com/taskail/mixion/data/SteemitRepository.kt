package com.taskail.mixion.data

import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.models.Tags
import com.taskail.mixion.data.source.local.LocalDataSource
import com.taskail.mixion.data.source.remote.RemoteDataSource

/**
 *Created by ed on 1/24/18.
 *
 * This class is responsible for all data
 */
class SteemitRepository(
        val remoteRepository: RemoteDataSource,
        val localRepository: LocalDataSource
        ) : SteemitDataSource {



    override fun  getFeed(callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>, sortBy: String) {

        remoteRepository.getFeed(callback, sortBy)
    }

    override fun getMoreFeed(callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>,
                             sortBy: String,
                             startAuthor: String, startPermLink: String) {

        remoteRepository.getMoreFeed(callback, sortBy, startAuthor, startPermLink)
    }

    override fun <T> getTags(callback: SteemitDataSource.DataLoadedCallback<T>) {


    }


    companion object {
        private var INSTANCE: SteemitRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance( remoteRepository: RemoteDataSource, localRepository: LocalDataSource):
                SteemitRepository{
            return INSTANCE ?: SteemitRepository(remoteRepository, localRepository).apply {
                INSTANCE = this
            }
        }
    }
}