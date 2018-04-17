package com.taskail.mixion.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.taskail.mixion.User
import com.taskail.mixion.main.steemitRepository

/**
 *Created by ed on 4/16/18.
 *
 * This service should be responsible for syncing data, such as tags, user votes,
 * and other user related data that we might want to cache into the local database.
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

            }, {
                Log.e(TAG, "Something went wrong ${it.message}")
                stopSelf()
            })
        }

    }
}