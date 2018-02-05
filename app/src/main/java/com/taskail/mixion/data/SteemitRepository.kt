package com.taskail.mixion.data

import android.util.Log
import com.google.common.collect.Lists
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

    /**
     * at this moment we are only querying remote for the users feed,
     * caching of the users feed may be implemented in the future
     */
    override fun getUserFeed(callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>) {
        remoteRepository.getUserFeed(callback)
    }

    override fun getMoreUserFeed(startAuthor: String, startPermLink: String, callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>) {
        remoteRepository.getMoreUserFeed(startAuthor, startPermLink, callback)
    }

    /**
     * It is sensible to only query the feed from remote
     */
    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>, sortBy: String) {
        remoteRepository.getFeed(callback, sortBy)
    }

    override fun getMoreFeed(callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>,
                             sortBy: String,
                             startAuthor: String, startPermLink: String) {

        remoteRepository.getMoreFeed(callback, sortBy, startAuthor, startPermLink)
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
    override fun getTags(callback: SteemitDataSource.DataLoadedCallback<RoomTags>) {

        //Tags are still in memory, respond immediately
        if (tagsInMemory.isNotEmpty()){

            callback.onDataLoaded(ArrayList(tagsInMemory.values))
        } else {
            // Query the local storage if available. If not, query the network.

            localRepository.getTags(object : SteemitDataSource.DataLoadedCallback<RoomTags>{

                override fun onDataLoaded(list: List<RoomTags>) {
                    refreshTagsInMemory(list)
                    callback.onDataLoaded(list)
                }

                override fun onDataLoaded(array: Array<RoomTags>) {
                    //Not needed...
                }

                override fun onLoadError(error: Throwable) {
                    getTagsFromRemoteDataSource(callback)
                }

            })
        }

    }

    /**
     * Getting tags from remote API will only be called if the local database is empty
     */
    private fun getTagsFromRemoteDataSource(callback: SteemitDataSource.DataLoadedCallback<RoomTags>){

        remoteRepository.getTags(object : SteemitDataSource.DataLoadedCallback<Tags>{
            override fun onDataLoaded(list: List<Tags>) {
                //Not Needed...
            }

            override fun onDataLoaded(array: Array<Tags>) {
                callback.onDataLoaded(handleRemoteToLocalTags(array))

            }

            override fun onLoadError(error: Throwable) {
                callback.onLoadError(error)
            }

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
    }
}