package com.taskail.mixion.steemdiscussion

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R

/**
 *Created by ed on 1/27/18.
 */
class DiscussionRecyclerViewAdapter(@NonNull private val discussionLayout: View) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DiscussionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        if (position == 0){
            return R.layout.layout_discussion_details
        }

        return R.layout.card_comments
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType){
            R.layout.layout_discussion_details -> DiscussionViewHolder(discussionLayout)
            else -> {
                CommentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_comments,
                        parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        var count = 1 // the discussion details

        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

    }
}