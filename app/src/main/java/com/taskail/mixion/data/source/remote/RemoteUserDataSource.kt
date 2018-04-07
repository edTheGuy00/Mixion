package com.taskail.mixion.data.source.remote

import com.taskail.mixion.data.UserDataSource
import com.taskail.mixion.data.models.AskSteemResult
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.network.AskSteemApi
import com.taskail.mixion.data.network.SteemAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 3/6/18.
 */

class RemoteUserDataSource(private val disposable: CompositeDisposable,
                           private val userMentions: AskSteemApi,
                           private val userApi: SteemAPI): UserDataSource.Remote {

    override var loadCount: Int = 10

    override fun getUserBlog(user: String, callback: UserDataSource.DataLoadedCallback<SteemDiscussion>) {
        fetchOnDisposable(callback, getUserBlog(user))
    }

    override fun getUserMentions(user: String, callback: UserDataSource.UserMentionsCallback) {
        fetchOnDisposable(callback, getUserMentions(user))
    }

    private fun getUserMentions(user: String): Observable<AskSteemResult>{
        return userMentions.askSteem(author = "\"\\@$user\"-author:$user",
                sort = "created",
                order = "desc",
                page = 1)
    }

    private fun getUserBlog(user: String): Observable<Array<SteemDiscussion>>{
        return userApi.getUserBlog("{\"tag\":\"$user\",\"limit\":\"$loadCount\"}")
    }

    private fun <T> fetchOnDisposable(callback: UserDataSource.DataLoadedCallback<T>,
                                      observable: Observable<Array<T>>){

        disposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) }))
    }

    private fun fetchOnDisposable(callback: UserDataSource.UserMentionsCallback,
                                  observable: Observable<AskSteemResult>){

        disposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) }))

    }
}