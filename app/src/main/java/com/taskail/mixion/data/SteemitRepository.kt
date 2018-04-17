package com.taskail.mixion.data

import com.google.common.collect.Lists
import com.taskail.mixion.data.models.AskSteemResult
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.data.models.Tags
import com.taskail.mixion.data.source.local.LocalDataSource
import com.taskail.mixion.data.source.local.RoomTags
import com.taskail.mixion.data.source.remote.RemoteDataSource
import java.util.*

/**
 *Created by ed on 1/24/18.
 *
 * This class is responsible for all data,
 * whether it is from the local database or the remote source, this class will
 * delegate where to query from.
 */
class SteemitRepository(
        val remoteRepository: RemoteDataSource,
        val localRepository: LocalDataSource
        ) : SteemitDataSource {

    override fun getUserFeed(response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        remoteRepository.getUserFeed(response, error)
    }

    override fun getMoreUserFeed(startAuthor: String, startPermLink: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        remoteRepository.getMoreUserFeed(startAuthor, startPermLink, response, error)
    }

    override fun getUserMentions(user: String,
                                 response: (AskSteemResult) -> Unit,
                                 error: (Throwable) -> Unit) {

        remoteRepository.getUserMentions(user, response, error)
    }

    override fun getUserBlog(user: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        remoteRepository.getUserBlog(user, response, error)
    }

    /**
     * It is sensible to only query the feed from remote
     */
    override fun getFeed(sortBy: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        remoteRepository.getFeed(sortBy, response, error)
    }

    override fun getMoreFeed(sortBy: String, startAuthor: String, startPermLink: String, response: (Array<SteemDiscussion>) -> Unit, error: (Throwable) -> Unit) {
        remoteRepository.getMoreFeed(sortBy, startAuthor, startPermLink, response, error)
    }

    /**
     * This get's a single discussion.
     */
    override fun getDiscussion(author: String, permlink: String, callBack: SteemitDataSource.DiscussionLoadedCallBack) {
        remoteRepository.getDiscussion(callBack, author, permlink)
    }

    /**
     * Tags are queried from the remote API only if the local database is empty.
     * Future call will always be from the local database, however we need to integrate
     * a method to update the local database once and a while.
     */
    var tagsInMemory: LinkedHashMap<String, RoomTags> = LinkedHashMap()

    override fun getTags(response: (List<RoomTags>) -> Unit, error: (Throwable) -> Unit) {
        if (tagsInMemory.isNotEmpty()){

            response(ArrayList(tagsInMemory.values))
        } else {
            localRepository.getTags({
                refreshTagsInMemory(it)
                response(it)
            }, {
                getTagsFromRemoteDataSource(response, error)
            })
        }
    }

    /**
     * Getting tags from remote API will only be called if the local database is empty
     */
    private fun getTagsFromRemoteDataSource(response: (List<RoomTags>) -> Unit,
                                            error: (Throwable) -> Unit){

        remoteRepository.getTags({
            response(handleRemoteToLocalTags(it))
        }, {
            error(it)
        })
    }

    /**
     * Tags loaded from the remote API will be saved into the local database,
     * first we need to convert them from and array of [Tags] into a list of [RoomTags] which is
     * the format of the local database
     */
    private fun handleRemoteToLocalTags(array: Array<Tags>) : List<RoomTags>{

        array.forEach {
            val newTag = RoomTags(it.name,
                    it.topPosts,
                    it.comments,
                    UUID.randomUUID().toString())

            tagsInMemory[newTag.id] = newTag
        }

        val newTags = Lists.newArrayList(tagsInMemory.values)

        refreshLocalDataSource(newTags)

        return newTags
    }

    /**
     * this is the action that saves into the local database
     */
    private fun refreshLocalDataSource(tags: List<RoomTags>){
        localRepository.deleteTags()

        for (tag in tags){
            localRepository.saveTags(tag)
        }
    }

    /**
     * We will keep the tags in memory so that we won't have to query
     * the database every time
     */
    private fun refreshTagsInMemory(list: List<RoomTags>){

        tagsInMemory.clear()

        list.forEach {
            cacheAndPerform(it) {}
        }

    }

    private inline fun cacheAndPerform(tag: RoomTags, perform: (RoomTags) -> Unit){

        val cacheTag = RoomTags(tag.tag,
                tag.posts,
                tag.comments,
                tag.id)

        tagsInMemory.put(cacheTag.id, cacheTag)

        perform(cacheTag)
    }

    companion object {
        private var INSTANCE: SteemitRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         */
        @JvmStatic
        fun getInstance( remoteRepository: RemoteDataSource, localRepository: LocalDataSource):
                SteemitRepository{
            return INSTANCE ?: SteemitRepository(remoteRepository, localRepository).apply {
                INSTANCE = this
            }
        }

        @JvmStatic
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}