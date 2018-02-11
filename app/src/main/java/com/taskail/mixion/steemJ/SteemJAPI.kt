package com.taskail.mixion.steemJ

import android.util.Log
import eu.bittrade.libs.steemj.SteemJ
import eu.bittrade.libs.steemj.base.models.AccountName
import eu.bittrade.libs.steemj.configuration.SteemJConfig
import eu.bittrade.libs.steemj.enums.PrivateKeyType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.apache.commons.lang3.tuple.ImmutablePair
import java.util.ArrayList

/**
 *Created by ed on 2/4/18.
 */

class SteemJAPI (private val steemJDisposable: CompositeDisposable) {

    var steemJ: SteemJ? = null
    val steemJConfig = SteemJConfig.getInstance()

    fun setupPostingUser(userName: String, postingKey: String){
        val privateKeys: ArrayList<ImmutablePair<PrivateKeyType, String>> = ArrayList()
        privateKeys.add(ImmutablePair(PrivateKeyType.POSTING, postingKey))

        steemJConfig.defaultAccount = AccountName(userName)
        steemJConfig.privateKeyStorage.addAccount(steemJConfig.defaultAccount, privateKeys)
    }

    fun connecToSteemit(){
        steemJDisposable.add(initSteemJ()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete{
                    Log.d("SteemJ", "Connected")
                }
                .doOnError{
                    Log.d("Error", it.message)
                }
                .subscribe())
    }

    fun createPost(title: String, body: String, tags: Array<String>){

        steemJDisposable.add(sendPost(title, body, tags)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError{
                    Log.d("emitter", it.message)
                }
                .subscribe{
                    Log.d("emitter", it)
                })

    }

    fun sendPost(title: String, body: String, tags: Array<String>):  Observable<String>{
        return Observable.create { emitter ->
            val newPost = steemJ?.createPost(title, body, tags)

            if (newPost != null)
                emitter.onNext(newPost.permlink.link)

        }
    }

    private fun initSteemJ() : Completable {
        return Completable.create {
            steemJ = SteemJ()
            it.onComplete()
        }
    }

}