package com.taskail.mixion.data

import com.taskail.mixion.data.source.remote.RemoteDataSource

/**
 *Created by ed on 1/24/18.
 *
 * This class is responsible for all data
 */
class SteemitRepository( val remoteRepository: RemoteDataSource) : SteemitDataSource {


    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback, sortBy: String) {

        remoteRepository.getFeed(callback, sortBy)
    }

    override fun getMoreFeed(callback: SteemitDataSource.DataLoadedCallback,
                             sortBy: String,
                             startAuthor: String, startPermLink: String) {

        remoteRepository.getMoreFeed(callback, sortBy, startAuthor, startPermLink)
    }


    companion object {
        private var INSTANCE: SteemitRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance( remoteRepository: RemoteDataSource):
                SteemitRepository{
            return INSTANCE ?: SteemitRepository(remoteRepository).apply {
                INSTANCE = this
            }
        }
    }
}