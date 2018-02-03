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

    private var isLoading = true
    private var noComments = false

    class DiscussionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun setComment(){

        }

    }

    override fun getItemViewType(position: Int): Int {

        return when (position){
            0 -> return R.layout.layout_discussion_details
            1 -> {
                if (isLoading)
                    return R.layout.item_loading
                else R.layout.card_comments
                if (noComments)
                    return R.layout.item_no_comments
                else R.layout.card_comments
            }
            else -> {
                R.layout.card_comments
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType){
            R.layout.layout_discussion_details -> DiscussionViewHolder(discussionLayout)
            R.layout.card_comments -> CommentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_comments,
                    parent, false))
            else -> {
                CommentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_comments,
                        parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        var count = 1 // discussion details

        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

    }
}