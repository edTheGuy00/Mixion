package com.taskail.mixion.steemJ

import com.taskail.mixion.DISCUSSION_STEEMJ_USER
import com.taskail.mixion.FEED_STEEMJ_USER
import com.taskail.mixion.CREATE_POST_STEEMJ_USER
import com.taskail.mixion.PROFILE_STEEMJ_USER
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

    private var feedIsActive = false
    private var profileIsActive = false
    private var createPostIsActive = false
    private var discussionIsActive = false

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

    private fun ensureSteemJActive() {
        if (steemJ == null){
            startSteemJ()
        }
    }

    fun registerSteemJUser(user: Int) {
        ensureSteemJActive()
        when (user) {
            FEED_STEEMJ_USER -> feedIsActive = true
            DISCUSSION_STEEMJ_USER -> discussionIsActive = true
            CREATE_POST_STEEMJ_USER -> createPostIsActive = true
            PROFILE_STEEMJ_USER -> profileIsActive = true
        }

    }

    fun deregisterSteemJUser(user: Int) {
        when (user) {
            FEED_STEEMJ_USER -> feedIsActive = false
            DISCUSSION_STEEMJ_USER -> discussionIsActive = false
            CREATE_POST_STEEMJ_USER -> createPostIsActive = false
            PROFILE_STEEMJ_USER -> profileIsActive = false
        }

        removeActive()
    }

    private fun removeActive() {
        if (!feedIsActive && !profileIsActive && !createPostIsActive) {
            disposable?.clear()
        }
    }

}