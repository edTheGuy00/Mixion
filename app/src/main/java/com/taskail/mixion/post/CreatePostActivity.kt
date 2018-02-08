package com.taskail.mixion.post

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.taskail.mixion.R
import com.taskail.mixion.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_create_post.*

/**
 *Created by ed on 2/7/18.
 */
class CreatePostActivity : BaseActivity() {

    companion object {
        @JvmStatic fun newIntent(context: Context): Intent {
            return Intent(context, CreatePostActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}