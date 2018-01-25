package com.taskail.mixion.data

import android.util.Log
import com.taskail.mixion.data.source.RemoteDataSource
import com.taskail.mixion.data.source.SteemAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/24/18.
 */
class SteemitRepository( val remoteRepository: RemoteDataSource ) : SteemitDataSource {


    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback, sortBy: String) {

    }

    override lateinit var tag: String

    override var loadCount = 10




    companion object {
        private var INSTANCE: SteemitRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance(disposable: CompositeDisposable,
                        remoteRepository: RemoteDataSource,
                        steemAPI: SteemAPI):
                SteemitRepository{
            return INSTANCE ?: SteemitRepository(remoteRepository).apply {
                INSTANCE = this
            }
        }
    }
}