package com.taskail.mixion.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.taskail.mixion.User
import com.taskail.mixion.data.models.local.UserVotes
import com.taskail.mixion.data.models.remote.AccountVotes
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.utils.transformStringToTimeStamp

/**
 * Created by ed on 4/16/18.
 * This service should be responsible for syncing date, such as tags, user votes,
 * and other user related date that we might want to cache into the local database.
 */

class DatabaseSync : IntentService("databaseSync") {

    companion object {

        fun newSyncIntent(context: Context): Intent {
            return Intent(context, DatabaseSync::class.java)
        }
    }

    val TAG = javaClass.simpleName

    override fun onHandleIntent(p0: Intent?) {

        if (User.userIsLoggedIn) {
            getUserVotes()
        } else {
            stopSelf()
        }
    }

    private fun getUserVotes() {
        val account = User.getUserName()
        if (account != null) {
            steemitRepository?.remoteRepository?.getAccountVotes(account, {
                saveRemoteVotesToDatabase(it)
            }, {
                Log.e(TAG, "Something went wrong ${it.message}")
                stopSelf()
            })
        }
    }

    /**
     * Votes loaded from the remote API will be saved into the local database,
     * we need to convert them from an array of [AccountVotes] into a
     * list of [UserVotes] then we sort the list by date and insert into the database
     */
    private fun saveRemoteVotesToDatabase(votes: Array<AccountVotes>){

        steemitRepository?.localRepository?.deleteVotes()

        // create a new list
        val newVotes = mutableListOf<UserVotes>()

        // add each vote from the reponse into the new list
        votes.forEach {

            val newVote = UserVotes(
                    it.authorperm,
                    it.time.transformStringToTimeStamp()
            )

           newVotes.add(newVote)
        }

        // sor the list by
        val sorted = newVotes.sortedWith(compareBy({ it.date }))

        Log.d(TAG, sorted.size.toString())

        sorted.forEach {
            steemitRepository?.localRepository?.saveVote(it)
            //Log.d(TAG, it.date.toString())
        }

        //getNewestVoteFromDatabase()
    }

    private fun getNewestVoteFromDatabase() {
        steemitRepository?.localRepository?.getVotes({
            it.forEach {
                Log.d(TAG, it.date.toString())
            }
        }, {
            Log.d(TAG, it.message)
        })

        stopSelf()
    }

}

