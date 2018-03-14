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

    fun upvote(author: String, permLink: String, percentage: Short) {
        steemJ?.upvote(author, permLink, percentage)
    }

    fun follow(userToFollow: String) {
        steemJ?.follow(userToFollow)
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