package com.taskail.mixion.data.source.local

import android.util.Log
import com.taskail.mixion.data.SteemitDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 *Created by ed on 1/26/18.
 *
 * This class is responsible for all data in the Local database
 */
class LocalDataSource(val draftsDao: DraftsDao,
                      val tagsDao: TagsDao,
                      val disposable: CompositeDisposable) : SteemitDataSource.Local {

    /**
     * get the tags from the local database
     */
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

    /**
     * insert tags into the local database
     */
    override fun saveTags(tags: RoomTags) {

        disposable.add(insertTag(tagsDao, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("Save Tag", "Success")
                }))

    }

    /**
     * This will delete all tags. Should only be called when we are updating the Database
     */
    override fun deleteTags() {
        disposable.add(deleteTags(tagsDao)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("Delete Tags", "Success")
                }))
    }

    override fun getDrafts(callback: SteemitDataSource.DataLoadedCallback<Drafts>) {
        disposable.add(getDraftsFromDatabase(draftsDao)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onDataLoaded(it)
                }, {
                    callback.onLoadError(it)
                }))
    }

    override fun saveDraft(draft: Drafts) {
        disposable.add(insertDraft(draftsDao, draft)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("Save Draft", "Success")
                }))
    }

    override fun deleteDraft(id: String) {
        disposable.add(deleteDraft(draftsDao, id)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.d("Delete Tags", "Success")
                }))
    }

    companion object {
        private var INSTANCE: LocalDataSource? = null

        @JvmStatic
        fun getInstance(draftsDao: DraftsDao, tagsDao: TagsDao, disposable: CompositeDisposable) : LocalDataSource{

            return INSTANCE ?: LocalDataSource(draftsDao, tagsDao, disposable).apply {
                INSTANCE = this
            }
        }
    }
}