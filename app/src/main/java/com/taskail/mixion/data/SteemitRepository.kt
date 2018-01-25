package com.taskail.mixion.data

import android.util.Log
import com.taskail.mixion.data.source.SteemAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/24/18.
 */
class SteemitRepository(val disposable: CompositeDisposable,
                        val steemAPI: SteemAPI) : SteemitDataSource {

    override lateinit var tag: String

    override var loadCount = 10


    override fun getNew(callback: SteemitDataSource.DataLoadedCallback) {

        Log.d("Repository", "get new called")

        disposable.add(steemAPI.getNewestDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) })
        )
    }

    override fun getTrending(callback: SteemitDataSource.DataLoadedCallback) {

        disposable.add(steemAPI.getTrendingDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) })
        )
    }

    override fun getHot(callback: SteemitDataSource.DataLoadedCallback) {

        disposable.add(steemAPI.getHotDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) })
        )
    }

    override fun getPromoted(callback: SteemitDataSource.DataLoadedCallback) {

        disposable.add(steemAPI.getPromotedDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) })
        )
    }

    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback) {

    }

    companion object {
        private var INSTANCE: SteemitRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance(disposable: CompositeDisposable, steemAPI: SteemAPI): SteemitRepository{
            return INSTANCE ?: SteemitRepository(disposable, steemAPI).apply {
                INSTANCE = this
            }
        }
    }
}