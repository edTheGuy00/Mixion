package com.taskail.mixion

import com.taskail.mixion.data.SteemitRepository
import com.taskail.mixion.data.UserRepository
import com.taskail.mixion.data.network.*
import com.taskail.mixion.data.source.local.LocalDataSource
import com.taskail.mixion.data.source.local.TagsDao
import com.taskail.mixion.data.source.remote.AskSteemRepository
import com.taskail.mixion.data.source.remote.RemoteDataSource
import com.taskail.mixion.data.source.remote.RemoteUserDataSource
import io.reactivex.disposables.CompositeDisposable

/**
 *Created by ed on 3/7/18.
 */

fun getSteemitRepository(remoteDisposable: CompositeDisposable,
                         localDisposable: CompositeDisposable,
                         tagsDao: TagsDao):
        SteemitRepository {
    return SteemitRepository
            .getInstance(createRemoteRepo(remoteDisposable),
                    createLocalRepo(tagsDao, localDisposable))
}

private fun createRemoteRepo(disposable: CompositeDisposable) : RemoteDataSource {
    return RemoteDataSource.getInstance(disposable, createSteemApi())
}

private fun createLocalRepo(tagsDao: TagsDao, disposable: CompositeDisposable): LocalDataSource {
    return LocalDataSource.getInstance(tagsDao, disposable)
}

private fun createSteemApi() : SteemAPI {
    return getRetrofitClient(baseUrl).create(SteemAPI::class.java)
}

fun getAskSteemRepo(disposable: CompositeDisposable) : AskSteemRepository {
    return AskSteemRepository.getInstance(disposable, createAskSteemApi())
}

fun getUserRepo(disposable: CompositeDisposable): UserRepository {
    return UserRepository.getInstance(createRemoteUserRepo(disposable))
}

private fun createRemoteUserRepo(disposable: CompositeDisposable): RemoteUserDataSource{
    return RemoteUserDataSource(disposable, createAskSteemApi(), createSteemApi())
}

private fun createAskSteemApi() : AskSteemApi {
    return getRetrofitClient(askSteemUrl).create(AskSteemApi::class.java)
}