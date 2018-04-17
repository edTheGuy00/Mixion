package com.taskail.mixion.data.source.local

import android.util.Log
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.local.Drafts
import com.taskail.mixion.data.models.local.RoomTags
import io.reactivex.Completable
import io.reactivex.Observable
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
                      val votesDao: VotesDao,
                      val disposable: CompositeDisposable) : SteemitDataSource.Local {

    private val TAG = javaClass.simpleName

    /**
     * get the tags from the local database
     */

    override fun getTags(response: (List<RoomTags>) -> Unit, error: (Throwable) -> Unit) {
        doOnDisposable(getTagsFromDatabase(tagsDao), response, error)
    }

    /**
     * insert tags into the local database
     */
    override fun saveTags(tags: RoomTags) {

        doOnCompletable(insertTag(tagsDao, tags), {
            Log.d(TAG, "Save Tag Success")
        }, {
            Log.e(TAG, it.message)
        })
    }

    /**
     * This will delete all tags. Should only be called when we are updating the Database
     */
    override fun deleteTags() {

        doOnCompletable(deleteTags(tagsDao), {
            Log.d(TAG, " Delete tag Success")
        }, {
            Log.e(TAG, it.message)
        })
    }

    override fun getDrafts(response: (List<Drafts>) -> Unit, error: (Throwable) -> Unit) {
        doOnDisposable(getDraftsFromDatabase(draftsDao), response, error)
    }

    override fun saveDraft(draft: Drafts) {
        doOnCompletable(insertDraft(draftsDao, draft), {
            Log.d(TAG, "Save Draft Success")
        }, {
            Log.e(TAG, it.message)
        })
    }

    override fun updateDraft(draft: Drafts) {
        doOnCompletable(updateDraft(draftsDao, draft), {
            Log.d(TAG, "Update Draft Success")
        }, {
            Log.e(TAG, it.message)
        })
    }

    override fun deleteDraft(id: String) {
        doOnCompletable(deleteDraft(draftsDao, id), {
            Log.d(TAG, "Delete Draft Success")
        }, {
            Log.e(TAG, it.message)
        })
    }

    private fun <T>doOnDisposable(observable: Observable<T>,
                                   response: (T) -> Unit,
                                   error: (Throwable) -> Unit) {
        disposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response(it)
                }, {
                    error(it)
                }))
    }

    private fun doOnCompletable(observable: Completable,
                                  response: () -> Unit,
                                  error: (Throwable) -> Unit) {
        disposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    response()
                }, {
                    error(it)
                }))
    }

    companion object {
        private var INSTANCE: LocalDataSource? = null

        @JvmStatic
        fun getInstance(draftsDao: DraftsDao, tagsDao: TagsDao, votesDao: VotesDao, disposable: CompositeDisposable) : LocalDataSource{

            return INSTANCE ?: LocalDataSource(draftsDao, tagsDao, votesDao, disposable).apply {
                INSTANCE = this
            }
        }
    }
}