package com.taskail.mixion.steemdiscussion

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.ContentReply
import com.taskail.mixion.utils.steemitutils.getFeedSummary
import kotlinx.android.synthetic.main.card_comments.view.*
import kotlinx.android.synthetic.main.layout_bottom_card_buttons.view.*

/**
 *Created by ed on 1/27/18.
 */
class DiscussionRecyclerViewAdapter(@NonNull private val discussionLayout: View,
                                    private val commentClick: (String, String) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoading = true
    private var noComments = !isLoading
    private val comments: ArrayList<ContentReply> = ArrayList<ContentReply>()

    class DiscussionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun setComment(comment: ContentReply,
                       commentClick: (String, String) -> Unit){

            with(comment){
                itemView.userName.text = author
                itemView.commentBody.text = body.getFeedSummary()

                itemView.payout.text = pendingPayoutValue.replace("SBD", "")
                itemView.votes_count.text = netVotes.toString()
                itemView.replies_count.text = children.toString()

                itemView.setOnClickListener {
                    commentClick(author, body)
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        return when (position){
            0 -> return R.layout.layout_discussion_details
            1 -> {
                if (isLoading && !noComments)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType){
            R.layout.layout_discussion_details -> DiscussionViewHolder(discussionLayout)
            R.layout.card_comments -> CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_comments,
                    parent, false))
            R.layout.item_no_comments -> SimpleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_no_comments,
                    parent, false))
            R.layout.item_loading -> SimpleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading,
                    parent, false))
            else -> {
                CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_comments,
                        parent, false))
            }
        }
    }

    fun addComments(commentsFromResponse: Array<ContentReply>){
        comments.addAll(commentsFromResponse)

        noComments = false
        removeLoadingIndicator()
        notifyItemRangeInserted(1, comments.size)
    }

    fun noComments(){
        noComments = true
        removeLoadingIndicator()
        notifyItemRangeInserted(1, 2)
    }

    private fun removeLoadingIndicator(){
        if (!isLoading)
            return
        isLoading = false
        notifyItemRemoved(1)
    }

    override fun getItemCount(): Int {
        var count = 1 // discussion details
        if (comments.isNotEmpty()){
            count += comments.size
        } else {
            count++ // either loading or no comments
        }
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)){
                R.layout.card_comments -> (holder as CommentViewHolder)
                        .setComment(comments[position-1],
                                commentClick)
        }
    }
}