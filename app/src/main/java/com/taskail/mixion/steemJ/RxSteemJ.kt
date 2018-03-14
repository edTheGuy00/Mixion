package com.taskail.mixion.steemJ

import android.util.Log
import eu.bittrade.libs.steemj.SteemJ
import eu.bittrade.libs.steemj.base.models.AccountName
import eu.bittrade.libs.steemj.base.models.Permlink
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException
import eu.bittrade.libs.steemj.exceptions.SteemResponseException
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 2/4/18.
 *
 * class for running [SteemJ] operations on RxJava.
 *
 * Only one instance of this class will be created by the [RxSteemJManager]
 *
 *
 */

class RxSteemJ(private val steemJDisposable: CompositeDisposable) {

    private val TAG= javaClass.simpleName

    var steemJ: SteemJ? = null

    fun connecToSteemit(){
        steemJDisposable.add(initSteemJ()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete{
                    Log.d(TAG, "Connected")
                }
                .doOnError{
                    Log.d(TAG, it.message)
                }
                .subscribe())
    }

    fun createPost(title: String, body: String, tags: Array<String>,
                   callback: SteemJCallback.CreatePostCallBack){

        steemJDisposable.add(createPostOperation(title, body, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{
                    callback.onError(it)
                }
                .doOnNext{
                    callback.onSuccess(it)
                }.subscribe())

    }

    fun follow(userToFollow: String, callback: SteemJCallback.SimpleCallback) {
        steemJDisposable.add(followOperation(userToFollow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete{
                    callback.onComplete()
                }
                .doOnError{
                    callback.onError()
                }.subscribe())
    }

    fun upvote(author: String, permLink: String, percentage: Short, callback: SteemJCallback.SimpleCallback) {
        steemJDisposable.add(upVoteOperation(author, permLink, percentage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete{
                    callback.onComplete()
                }
                .doOnError{
                    callback.onError()
                }.subscribe())
    }

    fun comment(author: String, permLink: String, body: String, tags: Array<String>,
                callback: SteemJCallback.CreatePostCallBack){

        steemJDisposable.add(createCommentOperation(author, permLink, body, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{
                    callback.onError(it)
                }
                .doOnNext{
                    callback.onSuccess(it)
                }.subscribe())
    }

    private fun createPostOperation(title: String, body: String, tags: Array<String>):  Observable<String>{
        return Observable.create { emitter ->

            try {
                val newPost = steemJ?.createPost(title, body, tags)

                if (newPost != null)
                    emitter.onNext(newPost.permlink.link)
            } catch (e: SteemCommunicationException) {
                emitter.onError(e)
            } catch (e: SteemResponseException) {
                emitter.onError(e)
            } catch (e: SteemInvalidTransactionException){
                emitter.onError(e)
            }

        }
    }

    private fun createCommentOperation(author: String, permLink: String, body: String, tags: Array<String>):  Observable<String>{
        return Observable.create { emitter ->

            try {
                val newPost = steemJ?.createComment(AccountName(author), Permlink(permLink), body, tags)

                if (newPost != null)
                    emitter.onNext(newPost.permlink.link)
            } catch (e: SteemCommunicationException) {
                emitter.onError(e)
            } catch (e: SteemResponseException) {
                emitter.onError(e)
            } catch (e: SteemInvalidTransactionException){
                emitter.onError(e)
            }

        }
    }

    private fun upVoteOperation(author: String, permLink: String, percentage: Short): Completable{
        return Completable.create {
            try {
                steemJ?.vote(AccountName(author), Permlink(permLink), percentage)
                it.onComplete()
            } catch (e: SteemCommunicationException) {
                it.onError(e)
            } catch (e: SteemResponseException) {
                it.onError(e)
            } catch (e: SteemInvalidTransactionException){
                it.onError(e)
            }
        }
    }

    private fun followOperation(userToFollow: String): Completable{
        return Completable.create {
            try {
                steemJ?.follow(AccountName(userToFollow))
                it.onComplete()
            } catch (e: SteemCommunicationException) {
                it.onError(e)
            } catch (e: SteemResponseException) {
                it.onError(e)
            } catch (e: SteemInvalidTransactionException){
                it.onError(e)
            }
        }
    }

    private fun initSteemJ() : Completable {
        return Completable.create {
            try {
                steemJ = SteemJ()
                it.onComplete()
            } catch (e: SteemCommunicationException) {
                it.onError(e)
            } catch (e: SteemResponseException) {
                it.onError(e)
            }

        }
    }


}