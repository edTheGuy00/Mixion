package com.taskail.mixion.data.source.remote

import eu.bittrade.libs.steemj.SteemJ
import eu.bittrade.libs.steemj.base.models.AccountName
import eu.bittrade.libs.steemj.base.models.Permlink
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers


var steemJClient: SteemJ? = null

fun initSteemJ(){
    getSteemJ().subscribeOn(Schedulers.io()).subscribe().dispose()
}

fun getSteemJ() : Completable{
    return Completable.create {
        steemJClient = SteemJ()

        it.onComplete()
    }
}

fun logIntoSteemJ(accountName: AccountName, password: String) : Completable{

    return Completable.create{
        steemJClient?.login(accountName, password)

        it.onComplete()
    }
}

fun upvote(accountName: AccountName, permlink: Permlink, percentage: Short) : Completable{

    return Completable.create{
        steemJClient?.vote(accountName, permlink, percentage)

        it.onComplete()
    }
}

fun reSteem(author: AccountName, permlink: Permlink) : Completable{
    return Completable.create{

        steemJClient?.reblog(author, permlink)

        it.onComplete()
    }
}
