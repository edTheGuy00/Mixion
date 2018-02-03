package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import `in`.uncod.android.bypass.style.ImageLoadingSpan
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.taskail.mixion.R
import com.taskail.mixion.utils.GlideImageGetter
import com.taskail.mixion.utils.ImageSpanTarget
import com.taskail.mixion.utils.steemitutils.fromHtml
import com.taskail.mixion.utils.steemitutils.parseMarkdownAndSetText
import com.taskail.mixion.utils.steemitutils.setTextWithNiceLinks
import kotlinx.android.synthetic.main.fragment_steem_discussion.*
import kotlinx.android.synthetic.main.layout_discussion_details.*
import kotlinx.android.synthetic.main.layout_discussion_details.view.*

/**
 *Created by ed on 1/27/18.
 */

class DiscussionDetailsFragment : Fragment(),
        DiscussionContract.View {

    override lateinit var presenter: DiscussionContract.Presenter

    private lateinit var titleAndDescriptionLayout: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_steem_discussion, container, false)

        titleAndDescriptionLayout = layoutInflater.inflate(R.layout.layout_discussion_details,
                discussion_comments, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val discussionAdapter = DiscussionRecyclerViewAdapter(titleAndDescriptionLayout)

        discussion_comments.itemAnimator = DefaultItemAnimator()
        discussion_comments.adapter = discussionAdapter
        discussion_comments.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


            }
        })

    }

    override fun displayTitle(title: String) {
        titleAndDescriptionLayout.discussion_title.text = title
    }

    override fun displayMarkdownBody(body: String, markdown: Bypass) {
        parseMarkdownAndSetText(titleAndDescriptionLayout.discussion_description,
                body, markdown, ImageCallBack())
    }


    override fun displayDtube() {
        //TODO - extract dtube video link, setup player
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