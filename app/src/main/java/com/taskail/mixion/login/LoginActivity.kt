package com.taskail.mixion.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.taskail.mixion.MixionApplication
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.profile.User
import kotlinx.android.synthetic.main.activity_login.*


/**
 *Created by ed on 2/3/18.
 */

class LoginActivity : BaseActivity() {

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent{
            return Intent(context, LoginActivity::class.java)
        }
    }

    private val keystoreCompat by lazy { (application as MixionApplication).keyStoreCompat }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginBtn.isEnabled = true

        loginBtn.setOnClickListener {
            val user = login_username_text.editText?.text.toString()
            val key = login_key_input.editText?.text.toString()

            if (user.isNotEmpty() && key.isNotEmpty()){
                storeAndSetUserCredentials(user, key)
            }
        }
    }

    private fun storeAndSetUserCredentials(user: String, key: String){
        keystoreCompat.storeSecret("${user};${key}", {
            Log.d("KeyStoreCompat", "Storing credentials failed")
        }, {
            User.storeUser(user, key)
        })
    }

}