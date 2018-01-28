package com.taskail.mixion.steemdiscussion

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion
import kotlinx.android.synthetic.main.fragment_steem_discussion.*
import kotlinx.android.synthetic.main.layout_discussion_title_and_body.view.*

/**
 *Created by ed on 1/27/18.
 */

class DiscussionDetailsFragment : Fragment() {

    lateinit var titleAndDescriptionLayout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_steem_discussion, container, false)

        titleAndDescriptionLayout = layoutInflater.inflate(R.layout.layout_discussion_details,
                discussion_comments, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DiscussionRecyclerViewAdapter(titleAndDescriptionLayout)

        discussion_comments.itemAnimator = DefaultItemAnimator()
        discussion_comments.adapter = adapter

    }

    fun loadDiscussion(discussion: SteemDiscussion){
        Log.d("Details Fragment", discussion.author)

        setTitle(discussion.title)
    }

    private fun setTitle(title: String){
       titleAndDescriptionLayout.discussion_title.text = title
    }

}