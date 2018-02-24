package com.taskail.mixion.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity

/**
 *Created by ed on 2/23/18.
 */

class ProfileActivity : BaseActivity(){

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

}