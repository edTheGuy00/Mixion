package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import `in`.uncod.android.bypass.style.ImageLoadingSpan
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.taskail.mixion.R
import com.taskail.mixion.User
import com.taskail.mixion.data.models.remote.ContentReply
import com.taskail.mixion.steemJ.RxSteemJManager
import com.taskail.mixion.steemJ.SteemJCallback
import com.taskail.mixion.ui.animation.RevealAnimationSettings
import com.taskail.mixion.utils.ImageSpanTarget
import com.taskail.mixion.utils.steemitutils.parseMarkdownAndSetText
import kotlinx.android.synthetic.main.fragment_steem_discussion.*
import kotlinx.android.synthetic.main.layout_discussion_details.*
import kotlinx.android.synthetic.main.layout_discussion_details.view.*

/**
 *Created by ed on 1/27/18.
 */

class DiscussionDetailsFragment : Fragment(),
        DiscussionContract.MainView{

    override lateinit var presenter: DiscussionContract.Presenter

    private lateinit var titleAndDescriptionLayout: View

    private lateinit var fragmentContainer: View

    private var videoPlayer: JZVideoPlayerStandard? = null

    private var jzVideOpen = false

    private val TAG = javaClass.simpleName

    private lateinit var discussionAdapter: DiscussionRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_steem_discussion, container, false)

        titleAndDescriptionLayout = layoutInflater.inflate(R.layout.layout_discussion_details,
                discussion_comments, false)

        fragmentContainer = view.findViewById(R.id.fragmentContainer)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        discussionAdapter = DiscussionRecyclerViewAdapter(titleAndDescriptionLayout,
                {
                    author, body, permLink, replies -> presenter.openCommentThread(author, body, permLink, replies)
                })

        discussion_comments.itemAnimator = DefaultItemAnimator()
        discussion_comments.adapter = discussionAdapter

        if (User.userIsLoggedIn) {
            fabComment.setOnClickListener {
                presenter.revealReplyFragment(constructRevealSettings())
            }
        } else {
            fabComment.hide()
        }



        //presenter.getUpVoteButtonFunction()
    }

    override fun setToVote(author: String, permLink: String) {
        titleAndDescriptionLayout.discussion_upvote_count.setOnClickListener {
            RxSteemJManager.upvote(author, permLink, 100,object : SteemJCallback.SimpleCallback {
                override fun onComplete() {
                    Log.d(TAG, "Upvoted Success")
                    titleAndDescriptionLayout.discussion_upvote_count.background = resources.getDrawable(R.color.colorAccent)



                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "something went wrong upvoting")
                }

            })
        }
    }

    override fun setToUnVote(author: String, permLink: String) {
        titleAndDescriptionLayout.discussion_upvote_count.background = resources.getDrawable(R.color.colorAccent)

        titleAndDescriptionLayout.discussion_upvote_count.setOnClickListener {
            RxSteemJManager.unvote(author, permLink, object : SteemJCallback.SimpleCallback {
                override fun onComplete() {
                    Log.d(TAG, "remove vote Success")
                    titleAndDescriptionLayout.discussion_upvote_count.background = null
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "something went wrong")
                }

            })
        }
    }

    private fun constructRevealSettings(): RevealAnimationSettings {
        val fabX = (fabComment.x + fabComment.width   /2).toInt()
        val fabY = (fabComment.y + fabComment.height / 2).toInt()

        val containerW = fragmentContainer.width
        val containerH = fragmentContainer.height

        return RevealAnimationSettings(fabX,
                fabY,
                containerW,
                containerH)
    }

    override fun displayTitle(title: String) {
        titleAndDescriptionLayout.discussion_title.text = title
    }

    override fun displayMarkdownBody(body: String, markdown: Bypass) {
        parseMarkdownAndSetText(titleAndDescriptionLayout.discussion_description,
                body, markdown, ImageCallBack())
    }

    override fun displayComments(commentsFromResponse: Array<ContentReply>) {
        discussionAdapter.addComments(commentsFromResponse)
    }

    override fun noComments() {
        discussionAdapter.noComments()
    }

    override fun displayDtube(videoImg: String?, videoUrl: String?) {
        titleAndDescriptionLayout.spacer.visibility = View.VISIBLE
        videoPlayerHolder.visibility = View.VISIBLE

        videoPlayer = JZVideoPlayerStandard(context)
        videoPlayerHolder.addView(videoPlayer)

        jzVideOpen = true

        videoPlayer?.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL)

    }

    override fun onBackPressed(): Boolean {
        return if (jzVideOpen) {
            JZVideoPlayer.backPress()
        } else {
            false
        }
    }

    override fun onPause() {
        super.onPause()
        if (jzVideOpen) {
            JZVideoPlayerStandard.releaseAllVideos()
        }
    }

    override fun displayYoutube(videoId: String) {
        //TODO - extract youtube id, setup player
    }

    override fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun displayBtnInfo(votes: String, payout: String, user: String, timeAgo: String) {
        titleAndDescriptionLayout.discussion_upvote_count.text = votes
        titleAndDescriptionLayout.discussion_payout.text = payout
        titleAndDescriptionLayout.discussion_author.text = user
        titleAndDescriptionLayout.time_ago.text = timeAgo
    }

    inner class ImageCallBack : Bypass.LoadImageCallback{
        override fun loadImage(src: String?, loadingSpan: ImageLoadingSpan?) {

            Glide.with(context)
                    .asBitmap()
                    .load(src)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .into(ImageSpanTarget(titleAndDescriptionLayout.discussion_description, loadingSpan!!))

        }
    }

}