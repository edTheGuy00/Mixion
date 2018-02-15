package com.taskail.mixion.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.taskail.mixion.MixionApplication
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import com.taskail.mixion.profile.User
import com.taskail.mixion.ui.TextInputValidator
import kotlinx.android.synthetic.main.activity_login.*


/**
 *Created by ed on 2/3/18.
 */

class LoginActivity : BaseActivity() {

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent{
            return Intent(context, LoginActivity::class.java)
        }

        val RESUlT_LOGIN_OK = 33
    }

    private val keystoreCompat by lazy { (application as MixionApplication).keyStoreCompat }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        TextInputValidator({
            loginBtn.isEnabled = it
        }, login_username_text, login_key_input)

        loginBtn.setOnClickListener {
            val user = login_username_text.editText?.text.toString()
            val key = login_key_input.editText?.text.toString()

            storeAndSetUserCredentials(user, key)
        }
    }

    private fun storeAndSetUserCredentials(user: String, key: String){
        keystoreCompat.storeSecret("${user};${key}", {
            Log.d("KeyStoreCompat", "Storing credentials failed")
        }, {
            User.storeUser(user, key)
            User.userIsLoggedIn = true
            setResult(RESUlT_LOGIN_OK)
            finish()
        })
    }

}