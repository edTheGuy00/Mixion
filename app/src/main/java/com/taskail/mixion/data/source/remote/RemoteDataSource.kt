package com.taskail.mixion.data.source.remote

import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.source.remote.SteemAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/24/18.
 *
 * This class is responsible for all Network data
 */
class RemoteDataSource(private val disposable: CompositeDisposable,
                       private val steemAPI: SteemAPI) : SteemitDataSource.Remote{

    override lateinit var tag: String
    override var loadCount: Int = 10


    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback, sortBy: String) {
        when(sortBy){

            "New" -> fetchOnDisposable(callback, getNew())

            "Hot" -> fetchOnDisposable(callback, getHot())

            "Promoted" -> fetchOnDisposable(callback, getPromoted())

            "Trending" -> fetchOnDisposable(callback, getTrending())
        }
    }

    override fun getMoreFeed(callback: SteemitDataSource.DataLoadedCallback,
                             sortBy: String,
                             startAuthor: String,
                             startPermLink: String) {

        when(sortBy){

            "New" -> fetchOnDisposable(callback, getMoreNew(startAuthor, startPermLink))

            "Hot" -> fetchOnDisposable(callback, getMoreHot(startAuthor, startPermLink))

            "Promoted" -> fetchOnDisposable(callback, getMorePromoted(startAuthor, startPermLink))

            "Trending" -> fetchOnDisposable(callback, getMoreTrending(startAuthor, startPermLink))
        }
    }

    private fun getNew(): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getHot(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getHotDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getPromoted(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getPromotedDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getTrending(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getTrendingDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getMoreNew(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getMoreHot(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getMorePromoted(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getMoreTrending(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
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