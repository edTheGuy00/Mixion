package com.taskail.mixion.steemJ

import io.reactivex.disposables.CompositeDisposable

/**
 *Created by ed on 3/13/18.
 *
 * Singleton manager object that creates a single instance of
 * [RxSteemJ]
 */
object RxSteemJManager {

    private var disposable: CompositeDisposable? = null

    private var steemJ: RxSteemJ? = null

    var feedIsActive = false
    var profileIsActive = false
    var createPostIsActive = false

    private fun startSteemJ(){

        disposable = CompositeDisposable()
        steemJ = RxSteemJ(disposable!!)
        steemJ?.connecToSteemit()

    }

    fun createPost(title: String, body: String, tags: Array<String>,
                   callback: SteemJCallback.CreatePostCallBack) {
        steemJ?.createPost(title, body, tags, callback)
    }

    fun upvote(author: String, permLink: String, percentage: Short, callback: SteemJCallback.SimpleCallback) {
        steemJ?.upvote(author, permLink, percentage, callback)
    }

    fun follow(userToFollow: String, callback: SteemJCallback.SimpleCallback) {
        steemJ?.follow(userToFollow, callback)
    }

    fun comment(author: String, permLink: String, body: String, tags: Array<String>, callback: SteemJCallback.CreatePostCallBack) {
        steemJ?.comment(author, permLink, body, tags, callback)
    }

    fun ensureSteemJActive() {
        if (steemJ == null){
            startSteemJ()
        }
    }

    fun removeActive() {
        if (!feedIsActive && !profileIsActive && !createPostIsActive) {
            disposable?.clear()
        }
    }

}