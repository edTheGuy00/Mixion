package com.taskail.mixion.steemdiscussion

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.ContentReply
import com.taskail.mixion.utils.steemitutils.getFeedSummary
import kotlinx.android.synthetic.main.card_comments.view.*

/**
 *Created by ed on 1/27/18.
 */
class DiscussionRecyclerViewAdapter(@NonNull private val discussionLayout: View) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLoading = true
    private var noComments = !isLoading
    private val comments: ArrayList<ContentReply> = ArrayList<ContentReply>()

    class DiscussionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun setComment(comment: ContentReply){

            with(comment){
                itemView.userName.text = author
                itemView.commentBody.text = body.getFeedSummary()
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        return when (position){
            0 -> return R.layout.layout_discussion_details
            1 -> {
                //if (isLoading && !noComments)
                //    return R.layout.item_loading
                //else R.layout.card_comments
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
            R.layout.item_no_comments -> SimpleViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_no_comments,
                    parent, false))
            R.layout.item_loading -> SimpleViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_loading,
                    parent, false))
            else -> {
                CommentViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_comments,
                        parent, false))
            }
        }
    }

    fun addComments(commentsFromResponse: Array<ContentReply>){
        comments.addAll(commentsFromResponse)

        Log.d("Add comments", comments[0].author)
        Log.d("Cooments size", comments.size.toString())
        noComments = false
        //removeLoadingIndicator()
        //notifyDataSetChanged()
        notifyItemRangeInserted(1, comments.size)
    }

    fun noComments(){
        noComments = true
        //removeLoadingIndicator()
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

            Log.d("Item Count", "Adding to count")
        } else {
            count++ // either loading or no comments
        }
        Log.d("Size", count.toString())
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)){
                R.layout.card_comments -> (holder as CommentViewHolder).setComment(comments[position-1])
        }
    }
}