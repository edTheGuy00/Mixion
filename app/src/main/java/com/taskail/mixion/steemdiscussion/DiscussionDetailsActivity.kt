package com.taskail.mixion.steemdiscussion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion

/**Created by ed on 10/6/17.
 */

internal const val openDiscussion = "DiscussionFromFeed"

fun newDiscussionIntent(context: Context, discussion: SteemDiscussion) : Intent{
    return Intent()
            .setClass(context, DiscussionDetailsActivity::class.java)
            .putExtra(openDiscussion, discussion)
}

class DiscussionDetailsActivity : AppCompatActivity() {

    private lateinit var detailsFragment: DiscussionDetailsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion_details)

        detailsFragment = supportFragmentManager
                .findFragmentById(R.id.discussion_details_fragment) as DiscussionDetailsFragment

        handleIntent(intent)
    }

    private fun handleIntent(@NonNull intent: Intent){

        val discussion: SteemDiscussion = intent.extras[openDiscussion] as SteemDiscussion

        detailsFragment.loadDiscussion(discussion)
    }
}
