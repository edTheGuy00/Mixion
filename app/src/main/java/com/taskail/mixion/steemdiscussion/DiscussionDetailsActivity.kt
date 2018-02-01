package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import com.taskail.mixion.R
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.utils.*
import com.taskail.mixion.utils.steemitutils.containsYoutubeVideo
import com.taskail.mixion.utils.steemitutils.getYoutubeId
import com.taskail.mixion.utils.steemitutils.isFromDmania
import com.taskail.mixion.utils.steemitutils.isFromDtube

/**Created by ed on 10/6/17.
 */

internal const val openDiscussion = "DiscussionFromFeed"

internal const val loadDiscussionAuthor = "LoadDiscussionAuthor"

internal const val loadDiscussionPermlink = "LoadDiscussionPermlink"

fun openDiscussionIntent(context: Context, discussion: SteemDiscussion) : Intent{
    return Intent()
            .setClass(context, DiscussionDetailsActivity::class.java)
            .putExtra(openDiscussion, discussion)
}

fun loadDiscussionIntent(context: Context, author: String, permlink: String): Intent{
    return Intent()
            .setClass(context, DiscussionDetailsActivity::class.java)
            .putExtra(loadDiscussionAuthor, author)
            .putExtra(loadDiscussionPermlink, permlink)
}

class DiscussionDetailsActivity : AppCompatActivity(),
        DiscussionContract.Presenter {

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
        } else {
            val author = intent.getStringExtra(loadDiscussionAuthor)
            val permlink = intent.getStringExtra(loadDiscussionPermlink)

            if (author != null && permlink !=null){
                loadDiscussion(author, permlink)
            }
        }
    }

    private fun loadDiscussion(author: String, permlink: String){
        steemitRepository?.getDiscussion(author, permlink, object : SteemitDataSource.DiscussionLoadedCallBack{
            override fun onDataLoaded(discussion: SteemDiscussion) {
                setDiscussion(discussion)
            }

            override fun onLoadError(error: Throwable) {

            }
        })
    }

    private fun setDiscussion(discussion: SteemDiscussion){
        discussionsView.displayTitle(discussion.title)
        discussionsView.setUser(discussion.author)
        discussionsView.setPayout(discussion.pendingPayoutValue.replace("SBD", ""))
        discussionsView.setTimeAgo(GetTimeAgo.getlongtoago(discussion.created))
        discussionsView.setUpVoteCount(discussion.netVotes.toString())

        val images = StringUtils.createArrayOfImages(discussion.jsonMetadata)
        if (images != null){
            discussionsView.displayImages(images)
        } else{
            discussionsView.setNoImages()
        }

        val format = StringUtils.getFormat(discussion.jsonMetadata)

        if (format != null){
            Log.d("format", format)
        }

        val body = discussion.body
        handleBody(body)

    }

    private fun handleBody(body: String){

        //TODO - Fix up a better solution,

        //val linkTextColor = ContextCompat.getColorStateList(this, R.color.colorPrimaryDark)!!
        //val linkHighLightColor = ContextCompat.getColor(this, R.color.colorAccent)


        val option = Bypass.Options()
                .setBlockQuoteLineColor(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setBlockQuoteLineWidth(2) // dps
                .setBlockQuoteLineIndent(8) // dps
                .setPreImageLinebreakHeight(4) //dps
                .setBlockQuoteIndentSize(TypedValue.COMPLEX_UNIT_DIP, 2f)
                .setBlockQuoteTextColor(ContextCompat.getColor(this, R.color.colorAccent))

        val bypass = Bypass(this, option)

        //val textToSet = parseBodyHtml(body, linkTextColor, linkHighLightColor)
        //discussionsView.displayHtmlBody(textToSet)

        //discussionsView.displayMarkdownBody(body, bypass)


        //For now we will try to only load videos from one source, if both exist
        when {
            body.isFromDtube() -> discussionsView.displayDtube()
            body.isFromDmania() -> { discussionsView.displaySimpleHtml(body) //TODO-

            }

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
