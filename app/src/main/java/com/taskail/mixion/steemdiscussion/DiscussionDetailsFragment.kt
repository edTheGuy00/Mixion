package com.taskail.mixion.steemdiscussion

import `in`.uncod.android.bypass.Bypass
import `in`.uncod.android.bypass.style.ImageLoadingSpan
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.utils.*
import kotlinx.android.synthetic.main.fragment_steem_discussion.*
import kotlinx.android.synthetic.main.layout_discussion_details.view.*

/**
 *Created by ed on 1/27/18.
 */

class DiscussionDetailsFragment : Fragment(), DiscussionContract.View {
    override lateinit var presenter: DiscussionContract.Presenter

    private lateinit var titleAndDescriptionLayout: View

    private lateinit var imagesAdapter: DiscussionImagesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_steem_discussion, container, false)

        titleAndDescriptionLayout = layoutInflater.inflate(R.layout.layout_discussion_details,
                discussion_comments, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val discussionAdapter = DiscussionRecyclerViewAdapter(titleAndDescriptionLayout)
        imagesAdapter = DiscussionImagesAdapter(ArrayList(0))

        discussion_comments.itemAnimator = DefaultItemAnimator()
        discussion_comments.adapter = discussionAdapter
        discussion_comments.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                imageRecycler.scrollBy(dy, dx)

            }
        })

        imageRecycler.itemAnimator = DefaultItemAnimator()
        imageRecycler.adapter = imagesAdapter
    }

    override fun displayTitle(title: String) {
        titleAndDescriptionLayout.discussion_title.text = title
    }

    override fun displayHtmlBody(body: CharSequence) {
        setTextWithNiceLinks(titleAndDescriptionLayout.discussion_description, body)

    }

    override fun displayMarkdownBody(body: String, markdown: Bypass) {
        parseMarkdownAndSetText(titleAndDescriptionLayout.discussion_description,
                body, markdown, ImageCallBack())
    }

    override fun displayImages(images: List<String>) {
        imagesAdapter.images = images
    }

    override fun setUpVoteCount(votes: String) {
        titleAndDescriptionLayout.discussion_upvote_count.text = votes
    }

    override fun setNoImages() {

    }

    inner class ImageCallBack : Bypass.LoadImageCallback{
        override fun loadImage(src: String?, loadingSpan: ImageLoadingSpan?) {

        }
    }

}