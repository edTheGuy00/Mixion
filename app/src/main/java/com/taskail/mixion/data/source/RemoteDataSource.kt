package com.taskail.mixion.data.source

import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.SteemDiscussion
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/24/18.
 */
class RemoteDataSource(private val disposable: CompositeDisposable,
                       private val steemAPI: SteemAPI) : SteemitDataSource{

    override lateinit var tag: String
    override var loadCount: Int = 10


    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback, sortBy: String) {
        when(sortBy){

            "New" -> fetchOnDisposable(callback, getNew())

            "Hot" -> fetchOnDisposable(callback, getHot())

            "Promoted" -> fetchOnDisposable(callback, getpromoted())

            "Trending" -> fetchOnDisposable(callback, getTrending())
        }
    }

    private fun getNew(): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getHot(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getHotDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getpromoted(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getPromotedDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getTrending(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getTrendingDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun fetchOnDisposable(callback: SteemitDataSource.DataLoadedCallback,
                                  observable: Observable<Array<SteemDiscussion>>){

        disposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) }))
    }

}