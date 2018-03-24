package com.taskail.mixion

import android.app.Application
import android.util.Log
import com.taskail.mixion.steemJ.setupSteemJUserSuccess
import cz.koto.keystorecompat.base.utility.runSinceLollipop
import cz.koto.keystorecompat.elplus.KeystoreCompat
import cz.koto.keystorecompat.elplus.compat.KeystoreCompatConfig


/**Created by ed on 10/22/17.
 */

var appInstance: MixionApplication? = null

class MixionApplication : Application() {

    lateinit var keyStoreCompat: KeystoreCompat

    private val TAG = javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        keyStoreCompat = KeystoreCompat.getInstance(this, KeyStoreConfig())

        checkUser()
    }

    private fun checkUser(){

        if (keyStoreCompat.hasSecretLoadable()) {
            runSinceLollipop {
                keyStoreCompat.loadSecretAsString({ decryptResults ->
                    decryptResults.split(';').let {
                        User.storeUser(it[0], it[1])
                        User.userIsLoggedIn = true
                        Log.i(TAG, "${it[0]} has been loaded")
                        initSteemJConfig(it[0], it[1])
                    }
                }, {
                    Log.d(TAG, it.message)
                }, User.forceLockScreenFlag)
            }
        }
    }

    private fun initSteemJConfig(user: String, key: String) {
        if (setupSteemJUserSuccess(user, key)) {
            Log.i(TAG, "config setupSuccess")
        } else {
            Log.e(TAG, "Unable to setup SteemJConfig")
            User.performLogout()
            keyStoreCompat.clearCredentials()
        }
    }

    inner class KeyStoreConfig: KeystoreCompatConfig(){

        //this will enable the keyStore lib to work on rooted phones
        override fun isRootDetectionEnabled(): Boolean {
            return false
        }

        //no need for the user to unlock/ enter pin onto the phone
        override fun getUserAuthenticationRequired(): Boolean {
            return false
        }
    }

}
