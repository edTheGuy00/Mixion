package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.taskail.mixion.R
import com.taskail.mixion.data.SteemitDataSource
import com.taskail.mixion.data.models.ContentReply
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.main.steemitRepository
import com.taskail.mixion.steemJ.RxSteemJManager
import com.taskail.mixion.steemJ.SteemJCallback
import com.taskail.mixion.ui.ElasticDragDismissFrameLayout
import com.taskail.mixion.ui.animation.DismissableAnimation
import com.taskail.mixion.ui.animation.RevealAnimationSettings
import com.taskail.mixion.utils.*
import com.taskail.mixion.utils.html2md.HTML2Md
import com.taskail.mixion.utils.steemitutils.getFirstImgFromJsonMeta
import com.taskail.mixion.utils.steemitutils.getVideoHash
import com.taskail.mixion.utils.steemitutils.getVideoUrl
import com.taskail.mixion.utils.steemitutils.isFromDtube
import kotlinx.android.synthetic.main.activity_discussion_details.*


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

    private lateinit var discussionsView: DiscussionContract.MainView

    private lateinit var chromeFader: ElasticDragDismissFrameLayout.SystemChromeFader

    private lateinit var discussionTitle: String
    private lateinit var discussionAuthor: String
    private lateinit var discussionPermlink: String

    private var replyFragment: CreateReplyFragment? = null
    private var replyFragmentIsOpen = false

    override fun start() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion_details)

        discussionsView = supportFragmentManager
                .findFragmentById(R.id.discussion_details_fragment) as DiscussionContract.MainView

        discussionsView.presenter = this

        chromeFader = object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {

            override fun onDragDismissed() {
                finish()
            }
        }

        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        activity_discussions_container.addListener(chromeFader)
    }

    override fun onPause() {
        activity_discussions_container.removeListener(chromeFader)
        super.onPause()
    }

    override fun revealReplyFragment(revealSettings: RevealAnimationSettings) {
        replyFragment = CreateReplyFragment.newInstance(revealSettings)
                .apply {
                    title = discussionTitle
            presenter = this@DiscussionDetailsActivity
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, replyFragment)
                .commit()

        replyFragmentIsOpen = true
    }

    private fun handleIntent(@NonNull intent: Intent) {
        val extra = intent.extras[openDiscussion]
        if (extra != null) {
            val discussion: SteemDiscussion = extra as SteemDiscussion
            setDiscussion(discussion)
        } else {
            val author = intent.getStringExtra(loadDiscussionAuthor)
            val permlink = intent.getStringExtra(loadDiscussionPermlink)

            if (author != null && permlink != null) {
                loadDiscussion(author, permlink)
            }
        }
    }

    private fun loadDiscussion(author: String, permlink: String) {
        steemitRepository?.getDiscussion(author, permlink, object : SteemitDataSource.DiscussionLoadedCallBack {
            override fun onDataLoaded(discussion: SteemDiscussion) {
                setDiscussion(discussion)
            }

            override fun onLoadError(error: Throwable) {
                //TODO - add loading error
            }
        })
    }

    private fun setDiscussion(discussion: SteemDiscussion) {
        discussionTitle = discussion.title
        discussionAuthor = discussion.author
        discussionPermlink = discussion.permlink
        discussionsView.displayTitle(discussionTitle)
        discussionsView.displayBtnInfo(discussion.netVotes.toString(),
                discussion.pendingPayoutValue.replace("SBD", ""),
                discussion.author,
                GetTimeAgo.getlongtoago(discussion.created))

        val newbody = HTML2Md.convert(discussion.body)
        discussionsView.displayMarkdownBody(newbody, getBypass())
        if (discussion.children > 0){
            loadComments(discussionAuthor, discussionPermlink)
        } else {
            discussionsView.noComments()
        }

        if (discussion.body.isFromDtube()){
            val img = discussion.body.getFirstImgFromJsonMeta()
            val hash = getVideoHash(discussion.jsonMetadata)
            if (!hash.isNullOrBlank()) {
                val videoUrl = getVideoUrl(hash!!)
                discussionsView.displayDtube(img, videoUrl)
            }
        }
    }

    private fun loadComments(author: String, permlink: String){
        steemitRepository?.remoteRepository?.getComments(author, permlink, object :
                SteemitDataSource.DataLoadedCallback<ContentReply> {
            override fun onDataLoaded(list: List<ContentReply>) {
                // Not needed, remove and fix this
            }

            override fun onDataLoaded(array: Array<ContentReply>) {
                discussionsView.displayComments(array)
            }

            override fun onLoadError(error: Throwable) {
            }

        })
    }

    override fun openCommentThread(author: String, body: String, permlink: String) {
        val bottomSheetDialogFragment = CommentsBottomSheet
                .newInstance().apply {
                    presenter = this@DiscussionDetailsActivity
                    commentAuthor = author
                    commentContent = body
                    commentPermLink = permlink
                }
        bottomSheetDialogFragment.show(supportFragmentManager,
                bottomSheetDialogFragment.tag)
    }

    override fun postDiscussionreply(content: String) {
        postReply(discussionAuthor, discussionPermlink, content)
    }

    override fun postCommentReply(author: String, permlink: String, content: String) {
        postReply(author, permlink, content)
    }

    private fun postReply(author: String, permlink: String, content: String) {
        val tags = arrayOf("mixion", "test")
        RxSteemJManager.comment(author, permlink, content, tags,
                object : SteemJCallback.CreatePostCallBack {
                    override fun onSuccess(permLink: String) {
                        if (replyFragmentIsOpen) {
                            closeReplyFragment()
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

    override fun dismissReplyFragment() {
        closeReplyFragment()
    }

    private fun closeReplyFragment() {
        (replyFragment as DismissableAnimation)
                .dismiss(object :
                        DismissableAnimation.OnDismissedListener {
                    override
                    fun onDismissed() {
                        supportFragmentManager
                                .beginTransaction()
                                .remove(replyFragment)
                                .commitAllowingStateLoss()
                    }
                })

        replyFragmentIsOpen = false
    }

    override fun onBackPressed() {
        if (discussionsView.onBackPressed()){
            return
        } else if (replyFragmentIsOpen) {
            closeReplyFragment()
            return
        }
        super.onBackPressed()
    }

    private fun getBypass(): Bypass{
        val option = Bypass.Options()
                .setBlockQuoteLineColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setBlockQuoteLineWidth(2) // dps
                .setBlockQuoteLineIndent(8) // dps
                .setPreImageLinebreakHeight(4) //dps
               .setBlockQuoteIndentSize(TypedValue.COMPLEX_UNIT_DIP, 2f)
                .setBlockQuoteTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        return Bypass(this, option)
    }
}