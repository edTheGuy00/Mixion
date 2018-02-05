package com.taskail.mixion

import android.app.Application
import cz.koto.keystorecompat.elplus.KeystoreCompat
import cz.koto.keystorecompat.elplus.compat.KeystoreCompatConfig


/**Created by ed on 10/22/17.
 */

var appInstance: MixionApplication? = null

class MixionApplication : Application() {

    lateinit var keyStoreCompat: KeystoreCompat

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        keyStoreCompat = KeystoreCompat.getInstance(this, KeyStoreConfig())

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
