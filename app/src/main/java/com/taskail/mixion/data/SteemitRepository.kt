package com.taskail.mixion.data

import com.taskail.mixion.data.source.RemoteDataSource

/**
 *Created by ed on 1/24/18.
 */
class SteemitRepository( val remoteRepository: RemoteDataSource ) : SteemitDataSource {


    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback, sortBy: String) {

        remoteRepository.getFeed(callback, sortBy)
    }

    override lateinit var tag: String

    override var loadCount = 10


    companion object {
        private var INSTANCE: SteemitRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance( remoteRepository: RemoteDataSource ):
                SteemitRepository{
            return INSTANCE ?: SteemitRepository(remoteRepository).apply {
                INSTANCE = this
            }
        }
    }
}