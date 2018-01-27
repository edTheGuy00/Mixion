package com.taskail.mixion.data.source.local

import android.support.annotation.NonNull
import io.reactivex.Completable
import io.reactivex.Observable

/**
 *Created by ed on 1/26/18.
 */

@NonNull
fun getTagsFromDatabase(@NonNull tagsDao: TagsDao): Observable<List<RoomTags>>{

    return Observable.create { emitter ->

        val tags = tagsDao.getTags()

        if (tags.isNotEmpty()){
            emitter.onNext(tags)
        } else{
            emitter.onError(Exception("Data Not Available"))
        }
     }
}

@NonNull
fun insertTag(@NonNull tagsDao: TagsDao, @NonNull tags: RoomTags) : Completable{
    return Completable.create{e ->
        tagsDao.insertTag(tags)
        e.onComplete()
    }
}