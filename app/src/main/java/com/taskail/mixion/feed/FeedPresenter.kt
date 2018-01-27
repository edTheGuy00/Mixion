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

    /**
     * Start follows onResume from FeedFragment
     */
    override fun start() {
        firstCall()
    }

    private fun firstCall(){

        if (!feedIsLoaded()){
            fetch()
        }
    }

    override fun sortBy(sortBy: String) {
        if (this.sortBy != sortBy){
            this.sortBy = sortBy
            performCleanFetch()
        }
    }

    private fun performCleanFetch(){
        feedView.discussionFromResponse.clear()
        feedView.clearItems()

        fetch()
    }

    override fun getByTag(tag: String) {
        if (steemitRepository.remoteRepository.tag != tag){
            steemitRepository.remoteRepository.tag = tag
            feedView.clearItems()
            performCleanFetch()
        }
    }

    private fun fetch(){

        steemitRepository.getFeed(object : SteemitDataSource.DataLoadedCallback<SteemDiscussion>{
            override fun onDataLoaded(list: List<SteemDiscussion>) {
            }

            override fun onDataLoaded(steem: Array<SteemDiscussion>) {
                feedView.discussionFromResponse.addAll(steem)
                feedView.showFeed()
            }

            override fun onLoadError(error: Throwable) {
            }

        }, sortBy)

    }

    override fun fetchMore(lastPostLocation: Int) {

        steemitRepository.getMoreFeed(object : SteemitDataSource.DataLoadedCallback<SteemDiscussion>{
            override fun onDataLoaded(list: List<SteemDiscussion>) {
            }

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

    /**
     * onResume will call a a query, check to see if items are empty before doing a network call
     */
    private fun feedIsLoaded(): Boolean{
        return feedView.discussionFromResponse.isNotEmpty()
    }

    /**
     * get the last author on the current list to fetch more items
     */
    private fun getStartAuthor(lastPostLocation: Int) : String{
        return feedView.discussionFromResponse[lastPostLocation-1].author
    }

    /**
     * get the last permlink on the current list to fetch more items
     */
    private fun getStartPermlink(lastPostLocation: Int) : String{
        return feedView.discussionFromResponse[lastPostLocation-1].permlink
    }
}