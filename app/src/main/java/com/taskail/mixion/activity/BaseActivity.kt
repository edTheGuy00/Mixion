package com.taskail.mixion.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 *Created by ed on 2/3/18.
 */
abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> false
        }
    }
}