package com.taskail.mixion.data.source.remote

import com.taskail.mixion.data.models.remote.AskSteemResult

/**
 *Created by ed on 1/29/18.
 */
interface AskSteemData {

    interface AskSteemCallback{

        fun onDataLoaded(askSteemResult: AskSteemResult)

        fun onLoadError(error: Throwable)
    }

    fun askSteem(term: String, sort: String, order: String, page: Int, callback: AskSteemCallback)

}