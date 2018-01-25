package com.taskail.mixion.data.source

import com.taskail.mixion.data.SteemitDataSource

/**
 *Created by ed on 1/24/18.
 */
class RemoteDataSource : SteemitDataSource{
    override var tag: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var loadCount: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun getNew(callback: SteemitDataSource.DataLoadedCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTrending(callback: SteemitDataSource.DataLoadedCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHot(callback: SteemitDataSource.DataLoadedCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPromoted(callback: SteemitDataSource.DataLoadedCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFeed(callback: SteemitDataSource.DataLoadedCallback) {

    }
}