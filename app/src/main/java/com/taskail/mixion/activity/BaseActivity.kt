package com.taskail.mixion.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.taskail.mixion.MixionApplication
import com.taskail.mixion.data.steemJConfig
import com.taskail.mixion.User
import cz.koto.keystorecompat.base.utility.runSinceLollipop

/**
 *Created by ed on 2/3/18.
 */
abstract class BaseActivity: AppCompatActivity() {

    protected val keystoreCompat by lazy { (application as MixionApplication).keyStoreCompat }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId)
        {
            android.R.id.home ->
            {
                onBackPressed()
                true
            }
            else -> false
        }
    }

    protected fun loginUser(user: String, key: String)
    {
        User.storeUser(user, key)
        User.userIsLoggedIn = true
    }

    protected fun redoLogIn()
    {
        runSinceLollipop {
            keystoreCompat.loadSecretAsString({ decryptResults ->
                decryptResults.split(';').let {
                    loginUser(it[0], it[1])
                }
            }, {
                Log.d("Error", it.message)
            }, User.forceLockScreenFlag)
        }
    }

    protected fun confirmUserLoggedIn(): Boolean
    {
        return User.getUserName() != null && User.getUserKey() != null
    }

    protected fun steemJUserReady(): Boolean
    {
        return !steemJConfig.defaultAccount.isEmpty
    }

    protected fun getUserName(): String
    {
        return User.getUserName()!!
    }

    protected fun getUserKey(): String
    {
        return User.getUserKey()!!
    }
}