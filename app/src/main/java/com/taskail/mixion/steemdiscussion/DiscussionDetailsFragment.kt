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
import cn.jzvd.JZVideoPlayer
import cn.jzvd.JZVideoPlayerStandard
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.taskail.mixion.BackPressedHandler
import com.taskail.mixion.R
import com.taskail.mixion.data.models.ContentReply
import com.taskail.mixion.utils.ImageSpanTarget
import com.taskail.mixion.utils.steemitutils.parseMarkdownAndSetText
import kotlinx.android.synthetic.main.fragment_steem_discussion.*
import kotlinx.android.synthetic.main.layout_discussion_details.view.*

/**
 *Created by ed on 1/27/18.
 */

class DiscussionDetailsFragment : Fragment(),
        DiscussionContract.View{

    override lateinit var presenter: DiscussionContract.Presenter

    private lateinit var titleAndDescriptionLayout: View

    private var videoPlayer: JZVideoPlayerStandard? = null

    lateinit var discussionAdapter: DiscussionRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_steem_discussion, container, false)

        titleAndDescriptionLayout = layoutInflater.inflate(R.layout.layout_discussion_details,
                discussion_comments, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        discussionAdapter = DiscussionRecyclerViewAdapter(titleAndDescriptionLayout,
                {
                    a, b -> openComment(a, b)
                })

        discussion_comments.itemAnimator = DefaultItemAnimator()
        discussion_comments.adapter = discussionAdapter

    }

    private fun openComment(author: String, body: String) {
        val bottomSheetDialogFragment = CommentsBottomSheet
                .newInstance(author, body)
        bottomSheetDialogFragment.show(childFragmentManager,
                bottomSheetDialogFragment.tag)

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


        videoPlayer?.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL)

    }

    override fun onBackPressed(): Boolean {
        return JZVideoPlayer.backPress()
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayerStandard.releaseAllVideos()
    }

    override fun displayYoutube(videoId: String) {
        //TODO - extract youtube id, setup player
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