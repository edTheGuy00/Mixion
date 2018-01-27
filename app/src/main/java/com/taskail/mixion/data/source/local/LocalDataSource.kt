package com.taskail.mixion.data.source.local

import android.util.Log
import com.taskail.mixion.data.SteemitDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/26/18.
 */
class LocalDataSource(val tagsDao: TagsDao,
                      val disposable: CompositeDisposable) : SteemitDataSource.Local {

    override fun getTags(callback: SteemitDataSource.DataLoadedCallback<RoomTags>) {

        disposable.add(getTagsFromDatabase(tagsDao)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onDataLoaded(it)
                }, {
                    callback.onLoadError(it)
                }))
    }

    override fun saveTags(tags: RoomTags) {

        disposable.add(insertTag(tagsDao, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("Save Business", "Success")
                }))

    }

    companion object {
        private var INSTANCE: LocalDataSource? = null

        @JvmStatic
        fun getInstance(tagsDao: TagsDao, disposable: CompositeDisposable) : LocalDataSource{

            return INSTANCE ?: LocalDataSource(tagsDao, disposable).apply {
                INSTANCE = this
            }
        }
    }
}