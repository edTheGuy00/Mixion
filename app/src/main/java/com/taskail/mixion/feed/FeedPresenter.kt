package com.taskail.mixion.feed

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
    var sortBy = "Trending"

    init {
        feedView.presenter = this
        feedView.discussionFromResponse = discussionFromResponse

        steemitRepository.remoteRepository.tag = "steemit"
    }

    override fun start() {
        fetch()
    }

    override fun fetch(){

        if (!feedIsLoaded()){
            getFeed()
        }
    }

    private fun getFeed(){

        steemitRepository.getFeed(object : SteemitDataSource.DataLoadedCallback{
            override fun onDataLoaded(steem: Array<SteemDiscussion>) {
                feedView.discussionFromResponse.addAll(steem)
                feedView.showFeed()
            }

            override fun onLoadError(error: Throwable) {
            }

        }, sortBy)

    }

    override fun fetchMore(lastPostLocation: Int) {

        steemitRepository.getMoreFeed(object : SteemitDataSource.DataLoadedCallback{
            override fun onDataLoaded(steem: Array<SteemDiscussion>) {

                /**
                 * Skip the first item from the returned list as it will be the same item
                 * from the previous last item.
                 */
                Collections.addAll(feedView.discussionFromResponse, *Arrays.copyOfRange(steem, 1, 10))

                feedView.showMoreFeed(lastPostLocation, feedView.discussionFromResponse.size)
            }

            override fun onLoadError(error: Throwable) {
            }

        }, sortBy, getStartAuthor(lastPostLocation), getStartPermlink(lastPostLocation))
    }

    private fun feedIsLoaded(): Boolean{
        return feedView.discussionFromResponse.isNotEmpty()
    }

    private fun getStartAuthor(lastPostLocation: Int) : String{
        return feedView.discussionFromResponse[lastPostLocation-1].author
    }

    private fun getStartPermlink(lastPostLocation: Int) : String{
        return feedView.discussionFromResponse[lastPostLocation-1].permlink
    }
}