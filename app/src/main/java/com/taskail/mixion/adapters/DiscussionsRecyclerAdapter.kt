package com.taskail.mixion.adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taskail.mixion.R
import com.taskail.mixion.models.SteemDiscussion
import com.taskail.mixion.adapters.DiscussionsRecyclerAdapter.ViewHolder
import com.taskail.mixion.utils.GetTimeAgo
import com.taskail.mixion.utils.StringUtils

/**
 *Created by ed on 11/17/17.
 */
class DiscussionsRecyclerAdapter(private val steemDiscussion: List<SteemDiscussion>, private val context: Context,
                                 private val listener: CardClickListener)
    : RecyclerView.Adapter<ViewHolder>() {

    private val options = RequestOptions().error(R.drawable.steem_logo).placeholder(R.drawable.steem_logo)
    private val stringManipulator = StringUtils()

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val body: TextView = itemView.findViewById(R.id.steem_body)
        val title: TextView = itemView.findViewById(R.id.steem_title)
        val author: TextView = itemView.findViewById(R.id.author)
        val category: TextView = itemView.findViewById(R.id.category)
        val payout: TextView = itemView.findViewById(R.id.payout)
        val votesCount: TextView = itemView.findViewById(R.id.votes_count)
        val repliesCount: TextView = itemView.findViewById(R.id.replies_count)
        val timeAgo: TextView = itemView.findViewById(R.id.time_ago)
        val discussionCard: CardView = itemView.findViewById(R.id.discussion_card)
        val mainImage: ImageView = itemView.findViewById(R.id.preview_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder{
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.card_steem, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.body?.text = stringManipulator.getShorterBody(steemDiscussion[position].body)
        holder?.title?.text = steemDiscussion[position].title
        holder?.author?.text = steemDiscussion[position].author
        holder?.category?.text = steemDiscussion[position].category
        holder?.payout?.text = steemDiscussion[position].pendingPayoutValue
        holder?.votesCount?.text = steemDiscussion[position].netVotes.toString()
        holder?.repliesCount?.text = steemDiscussion[position].children.toString()
        holder?.timeAgo?.text = GetTimeAgo.getlongtoago(steemDiscussion[position].created)

        Glide.with(context)
                .load(stringManipulator.getFirstImageFromJsonMetaData(steemDiscussion[position].jsonMetadata))
                .apply(options)
                .into(holder?.mainImage)

        holder?.discussionCard?.setOnClickListener({ listener.onCardClicked(position) })
    }

    override fun getItemCount(): Int {
        Log.d("adapter ", "size " + steemDiscussion.size)
        return steemDiscussion.size
    }

    interface CardClickListener {
        fun onCardClicked(position: Int)
    }

}