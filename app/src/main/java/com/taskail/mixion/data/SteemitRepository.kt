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
 * This class is responsible for all data
 */
class SteemitRepository(
        val remoteRepository: RemoteDataSource,
        val localRepository: LocalDataSource
        ) : SteemitDataSource {


    override fun  getFeed(callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>, sortBy: String) {

        remoteRepository.getFeed(callback, sortBy)
    }

    override fun getMoreFeed(callback: SteemitDataSource.DataLoadedCallback<SteemDiscussion>,
                             sortBy: String,
                             startAuthor: String, startPermLink: String) {

        remoteRepository.getMoreFeed(callback, sortBy, startAuthor, startPermLink)
    }

    var tagsInMemory: LinkedHashMap<String, RoomTags> = LinkedHashMap()
    override fun getTags(callback: SteemitDataSource.DataLoadedCallback<RoomTags>) {

        Log.d("tags", "get tags")

        //Tags are still in memory, respond immediately
        if (tagsInMemory.isNotEmpty()){

            Log.d("tags", "memory not empty")

            callback.onDataLoaded(ArrayList(tagsInMemory.values))
        } else {
            // Query the local storage if available. If not, query the network.

            localRepository.getTags(object : SteemitDataSource.DataLoadedCallback<RoomTags>{

                override fun onDataLoaded(list: List<RoomTags>) {

                    Log.d("tags", "from local database")
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

    override fun getDiscussion(author: String, permlink: String, callBack: SteemitDataSource.DiscussionLoadedCallBack) {
        remoteRepository.getDiscussion(callBack, author, permlink)
    }

    private fun getTagsFromRemoteDataSource(callback: SteemitDataSource.DataLoadedCallback<RoomTags>){

        Log.d("tags", "getting from network")

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

    private fun refreshLocalDataSource(tags: List<RoomTags>){
        localRepository.deleteTags()

        for (tag in tags){
            localRepository.saveTags(tag)
        }

    }

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