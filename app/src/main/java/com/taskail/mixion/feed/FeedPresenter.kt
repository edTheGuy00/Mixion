package com.taskail.mixion.feed

import android.util.Log
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.models.SteemDiscussion
import java.util.*

/**
 *Created by ed on 1/24/18.
 */
class FeedPresenter(val feedView: FeedContract.View,
                    val steemitRepository: SteemitRepository) : FeedContract.Presenter {

    var discussionFromResponse = ArrayList<SteemDiscussion>()
    var loadCount: Int = 0

    init {
        feedView.presenter = this
        feedView.discussionFromResponse = discussionFromResponse

        steemitRepository.remoteRepository.tag = "steemit"

        steemitRepository.tag = "steemit"
    }

    override fun start() {
        fetch()
    }

    private fun fetch(){
        steemitRepository.getFeed(object : SteemitDataSource.DataLoadedCallback{
            override fun onDataLoaded(steem: Array<SteemDiscussion>) {
                feedView.discussionFromResponse.addAll(steem)

                feedView.showFeed()
            }

            override fun onLoadError(error: Throwable) {
            }

            override fun onComplete() {
            }

        }, "New")
    }

    override fun loadSteemFeed(steem : Array<SteemDiscussion>) {


        feedView.discussionFromResponse.addAll(steem)
    }

    override fun loadMoreSteem(steem : Array<SteemDiscussion>) {
        discussionFromResponse.addAll(Arrays.copyOfRange(steem, 1, loadCount))
    }


}