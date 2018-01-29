package com.taskail.mixion.steemdiscussion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.utils.GetTimeAgo
import com.taskail.mixion.utils.StringUtils
import com.taskail.mixion.utils.parseHtml

/**Created by ed on 10/6/17.
 */

internal const val openDiscussion = "DiscussionFromFeed"

internal const val loadDiscussionDiscussion = "LoadDiscussion"

fun newDiscussionIntent(context: Context, discussion: SteemDiscussion) : Intent{
    return Intent()
            .setClass(context, DiscussionDetailsActivity::class.java)
            .putExtra(openDiscussion, discussion)
}

class DiscussionDetailsActivity : AppCompatActivity(), DiscussionContract.Presenter {

    private lateinit var discussionsView: DiscussionContract.View

    override fun start() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion_details)

        discussionsView = supportFragmentManager
                .findFragmentById(R.id.discussion_details_fragment) as DiscussionContract.View

        discussionsView.presenter = this

        handleIntent(intent)
    }

    private fun handleIntent(@NonNull intent: Intent){
        val extra = intent.extras[openDiscussion]
        if (extra != null){
            val discussion: SteemDiscussion = extra as SteemDiscussion
            setDiscussion(discussion)
        }
    }

    private fun setDiscussion(discussion: SteemDiscussion){
        discussionsView.displayTitle(discussion.title)
        discussionsView.setUser(discussion.author)
        discussionsView.setPayout(discussion.pendingPayoutValue.replace("SBD", ""))
        discussionsView.setTimeAgo(GetTimeAgo.getlongtoago(discussion.created))
        discussionsView.setUpVoteCount(discussion.netVotes.toString())

        val textToSet = parseHtml(discussion.body, ContextCompat.getColorStateList(this, R.color.colorPrimaryDark)!!,
                ContextCompat.getColor(this, R.color.colorAccent))

        discussionsView.displayHtmlBody(textToSet)

        val images = StringUtils.createArrayOfImages(discussion.jsonMetadata)

        if (images != null){
            discussionsView.displayImages(images)
        } else{
            discussionsView.setNoImages()
        }

        //val format = StringUtils.getFormat(discussion.jsonMetadata)

        //TODO - properly parse the body
        /**if (format != null)
            when(format){
                "html" -> {
                    val textToSet = parseHtml(discussion.body, ContextCompat.getColorStateList(this, R.color.colorPrimaryDark)!!,
                            ContextCompat.getColor(this, R.color.colorAccent))

                    discussionsView.displayHtmlBody(textToSet)
                }

                "markdown" -> {
                    val option = Bypass.Options()

                    val bypass = Bypass(this, option)

                    discussionsView.displayMarkdownBody(discussion.body, bypass)
                }
            } */
    }
}
