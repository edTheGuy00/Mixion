package com.taskail.mixion.data.source.remote

import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.*
import com.taskail.mixion.data.network.AskSteemApi
import com.taskail.mixion.data.network.SteemAPI
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/24/18.
 *
 * This class is responsible for all Network data
 */
class RemoteDataSource(private val disposable: CompositeDisposable,
                       private val askSteemApi: AskSteemApi,
                       private val steemAPI: SteemAPI) :
        SteemitDataSource.Remote{

    override lateinit var tag: String
    override var loadCount: Int = 10

    override fun getUserFeed(response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        getOnDisposable(getUserFeed(), response, error)
    }

    override fun getUserMentions(user: String,
                                 response: (AskSteemResult) -> Unit,
                                 error: (Throwable) -> Unit) {
        getOnDisposable(getUserMentions(user), response, error)
    }

    private fun getUserMentions(user: String): Observable<AskSteemResult>{
        return askSteemApi.askSteem(author = "\"\\@$user\"-author:$user",
                sort = "created",
                order = "desc",
                page = 1)
    }

    override fun getMoreUserFeed(startAuthor: String, startPermLink: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        getOnDisposable(getMoreUserFeed(startAuthor, startPermLink), response, error)
    }

    override fun getUserBlog(user: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        getOnDisposable(getUserBlog(user), response, error)
    }

    override fun getFeed(sortBy: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        when(sortBy){

            "New" -> getOnDisposable(getNew(), response, error)

            "Hot" -> getOnDisposable(getHot(), response, error)

            "Promoted" -> getOnDisposable(getPromoted(), response, error)

            "Trending" -> getOnDisposable(getTrending(), response, error)
        }
    }

    override fun getMoreFeed(sortBy: String, startAuthor: String, startPermLink: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        when(sortBy){

            "New" -> getOnDisposable(getMoreNew(startAuthor, startPermLink), response, error)

            "Hot" -> getOnDisposable(getMoreHot(startAuthor, startPermLink), response, error)

            "Promoted" -> getOnDisposable(getMorePromoted(startAuthor, startPermLink), response, error)

            "Trending" -> getOnDisposable(getMoreTrending(startAuthor, startPermLink), response, error)
        }
    }

    override fun getTags(response: (Array<Tags>) -> Unit, error: (Throwable) -> Unit) {
        getOnDisposable(getTags(), response, error)
    }

    override fun getComments(author: String, permlink: String, response: (Array<ContentReply>) -> Unit, error: (Throwable) -> Unit) {
        getOnDisposable(getComments(author, permlink), response, error)
    }

    override fun getDiscussion(author: String, permlink: String, response: (SteemDiscussion) -> Unit, error: (Throwable) -> Unit) {
        getOnDisposable(getDiscussion(author, permlink), response, error)
    }

    override fun getAccountVotes(user: String,
                                 response: (Array<AccountVotes>) -> Unit,
                                 error: (Throwable) -> Unit) {
        getOnDisposable(steemAPI.getAccountVotes(user), response, error)
    }

    private fun getUserFeed(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getUserFeed("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getMoreUserFeed(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>>{
        return steemAPI.getUserFeed("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getUserBlog(user: String): Observable<Array<SteemDiscussion>>{
        return steemAPI.getUserBlog("{\"tag\":\"$user\",\"limit\":\"$loadCount\"}")
    }

    private fun getNew(): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getHot(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getHotDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getPromoted(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getPromotedDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getTrending(): Observable<Array<SteemDiscussion>>{
        return steemAPI.getTrendingDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\"}")
    }

    private fun getMoreNew(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getMoreHot(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getMorePromoted(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getMoreTrending(startAuthor: String, startPermLink: String): Observable<Array<SteemDiscussion>> {
        return steemAPI.getNewestDiscussions("{\"tag\":\"$tag\",\"limit\":\"$loadCount\", \"start_author\":\"$startAuthor\", \"start_permlink\":\"$startPermLink\"}")
    }

    private fun getDiscussion(author: String, permlink: String) : Observable<SteemDiscussion>{
        return steemAPI.getContent(author, permlink)
    }

    private fun getTags() : Observable<Array<Tags>>{
        return steemAPI.getTags("life", 100)
    }

    private fun getComments(author: String, permlink: String): Observable<Array<ContentReply>>{
        return steemAPI.getContentReplies(author, permlink)
    }

    private fun <T> getOnDisposable(observable: Observable <T>,
                                    response: (T) -> Unit,
                                    error: (Throwable) -> Unit) {

        disposable.add(observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            response(it)
                        },
                        {
                            error(it)
                        }
                )
        )

    }

    companion object {
        private var INSTANCE: RemoteDataSource? = null

        @JvmStatic
        fun getInstance(disposable: CompositeDisposable,
                        steemAPI: SteemAPI,
                        askSteemApi: AskSteemApi) : RemoteDataSource{

            return INSTANCE ?: RemoteDataSource(disposable, askSteemApi, steemAPI).apply {
                INSTANCE = this
            }
        }
        @JvmStatic
        fun destroyInstance(){
            INSTANCE = null
        }
    }

}