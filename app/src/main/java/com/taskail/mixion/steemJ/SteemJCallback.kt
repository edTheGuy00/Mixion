package com.taskail.mixion.steemJ

/**
 *Created by ed on 3/14/18.
 */

interface SteemJCallback {

    interface CreatePostCallBack {

        fun onSuccess(permLink: String)

        fun onError(e: Throwable)
    }
}
