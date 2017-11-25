package com.taskail.mixion.utils

import android.content.Context
import android.content.Intent
import com.taskail.mixion.activities.SearchActivity

/**
 *Created by ed on 11/25/17.
 */
class ActivityLauncherUtil {

    companion object {

        @JvmStatic fun showSearchActivity(context: Context){
            val intent = Intent  (context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }
}