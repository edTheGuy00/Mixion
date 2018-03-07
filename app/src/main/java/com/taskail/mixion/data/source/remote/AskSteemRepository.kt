package com.taskail.mixion.data.source.remote

import com.taskail.mixion.data.models.AskSteemResult
import com.taskail.mixion.data.network.AskSteemApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/29/18.
 */
class AskSteemRepository(private val disposable: CompositeDisposable,
                         private val askSteemApi: AskSteemApi) : AskSteemData {

    override fun askSteem(term: String, callback: AskSteemData.AskSteemCallback) {
        fetchOnDisposable(callback, askSteem(term))
    }

    override fun askMore(term: String, page: Int, callback: AskSteemData.AskSteemCallback) {
        fetchOnDisposable(callback, askMore(term, page))
    }

    private fun askSteem(term: String) : Observable<AskSteemResult> {
        return askSteemApi.askSteem(term)
    }

    private fun askMore(term: String, page: Int) : Observable<AskSteemResult> {
        return askSteemApi.askMore(term, page)
    }

    private fun fetchOnDisposable(callback: AskSteemData.AskSteemCallback,
                                  observable: Observable<AskSteemResult>){

        disposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { callback.onDataLoaded(it)},
                        { callback.onLoadError(it) }))

    }

    companion object {
        private var INSTANCE: AskSteemRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance( disposable: CompositeDisposable, askSteemApi: AskSteemApi):
                AskSteemRepository {
            return INSTANCE
                    ?: AskSteemRepository(disposable, askSteemApi).apply {
                INSTANCE = this
            }
        }
    }
}