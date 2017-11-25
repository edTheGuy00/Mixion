package com.taskail.mixion.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.taskail.mixion.R
import com.taskail.mixion.adapters.CommentsRecyclerAdapter.ViewHolder
import com.taskail.mixion.models.ContentReply

/**
 *Created by ed on 11/17/17.
 * Comments, (replies) of a discussion
 */
class CommentsRecyclerAdapter (private val discussionReplies: List<ContentReply>, private val context: Context) :
        RecyclerView.Adapter<ViewHolder>(){

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val userName: TextView = itemView.findViewById(R.id.userName)
        val content: TextView = itemView.findViewById(R.id.commentBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder{
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.card_comments, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.userName?.text = discussionReplies[position].author
        holder?.content?.text = discussionReplies[position].body
    }

    override fun getItemCount(): Int {
        return discussionReplies.size
    }
}