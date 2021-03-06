package com.taskail.mixion.data.source.local

import android.support.annotation.NonNull
import com.taskail.mixion.data.models.local.Drafts
import com.taskail.mixion.data.models.local.RoomTags
import com.taskail.mixion.data.models.local.UserVotes
import com.taskail.mixion.voteDoesNotExist
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
fun searchVotesFromDatabase(@NonNull votesDao: VotesDao, authorperm: String): Observable<List<UserVotes>>{

    return Observable.create { emitter ->

        val votes = votesDao.findVote(authorperm)

        if (votes.isNotEmpty()){
            emitter.onNext(votes)
        } else{
            emitter.onError(Exception(voteDoesNotExist))
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

@NonNull
fun insertVote(@NonNull votesDao: VotesDao, @NonNull userVotes: UserVotes) : Completable{
    return Completable.create{e ->
        votesDao.insertVotes(userVotes)
        e.onComplete()
    }
}

@NonNull
fun getVotesFromDatabase(@NonNull votesDao: VotesDao): Observable<List<UserVotes>>{

    return Observable.create { emitter ->

        val votes = votesDao.getVotes()

        if (votes.isNotEmpty()){
            emitter.onNext(votes)
        } else{
            emitter.onError(Exception("Empty Database"))
        }
    }
}

@NonNull
fun deleteVotesFromDatabase(@NonNull votesDao: VotesDao) : Completable{
    return Completable.create { emitter ->
        votesDao.deleteVotes()
        emitter.onComplete()
    }
}

@NonNull
fun deleteTags(@NonNull tagsDao: TagsDao) : Completable{
    return Completable.create { emitter ->
        tagsDao.deleteTags()
        emitter.onComplete()
    }
}

@NonNull
fun getDraftsFromDatabase(@NonNull draftsDao: DraftsDao): Observable<List<Drafts>>{

    return Observable.create { emitter ->

        val drafts = draftsDao.getDrafts()

        if (drafts.isNotEmpty()){
            emitter.onNext(drafts)
        } else{
            emitter.onError(Exception("Data Not Available"))
        }
    }
}

@NonNull
fun insertDraft(@NonNull draftsDao: DraftsDao, @NonNull draft: Drafts) : Completable{
    return Completable.create{e ->
        draftsDao.insertDraft(draft)
        e.onComplete()
    }
}

@NonNull
fun updateDraft(@NonNull draftsDao: DraftsDao, @NonNull draft: Drafts) : Completable{
    return Completable.create{e ->
        draftsDao.insertDraft(draft)
        e.onComplete()
    }
}

@NonNull
fun deleteDraft(@NonNull draftsDao: DraftsDao, id: String) : Completable{
    return Completable.create { emitter ->
        draftsDao.deleteById(id)
        emitter.onComplete()
    }
}
