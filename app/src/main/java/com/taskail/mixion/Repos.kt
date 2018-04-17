package com.taskail.mixion

import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.network.*
import com.taskail.mixion.data.source.local.DraftsDao
import com.taskail.mixion.data.source.local.LocalDataSource
import com.taskail.mixion.data.source.local.TagsDao
import com.taskail.mixion.data.source.remote.AskSteemRepository
import com.taskail.mixion.data.source.remote.RemoteDataSource
import io.reactivex.disposables.CompositeDisposable

/**
 *Created by ed on 3/7/18.
 */

fun getMixionRepository(remoteDisposable: CompositeDisposable,
                         localDisposable: CompositeDisposable,
                         draftsDao: DraftsDao,
                         tagsDao: TagsDao):
        SteemitRepository {
    return SteemitRepository
            .getInstance(createRemoteRepo(remoteDisposable),
                    createLocalRepo(draftsDao, tagsDao, localDisposable))
}

private fun createRemoteRepo(disposable: CompositeDisposable) : RemoteDataSource {
    return RemoteDataSource.getInstance(disposable, createSteemApi(), createAskSteemApi())
}

private fun createLocalRepo(draftsDao: DraftsDao, tagsDao: TagsDao, disposable: CompositeDisposable): LocalDataSource {
    return LocalDataSource.getInstance(draftsDao, tagsDao, disposable)
}

private fun createSteemApi() : SteemAPI {
    return getRetrofitClient(baseUrl).create(SteemAPI::class.java)
}

fun getAskSteemRepo(disposable: CompositeDisposable) : AskSteemRepository {
    return AskSteemRepository.getInstance(disposable, createAskSteemApi())
}

private fun createAskSteemApi() : AskSteemApi {
    return getRetrofitClient(askSteemUrl).create(AskSteemApi::class.java)
}