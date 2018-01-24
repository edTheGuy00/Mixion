package com.taskail.mixion.feed

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.taskail.mixion.R
import com.taskail.mixion.data.models.SteemDiscussion
import com.taskail.mixion.utils.StringUtils
import com.taskail.mixion.utils.getFirstImgFromJsonMeta
import kotlinx.android.synthetic.main.cardview_steem_post.view.*
import kotlinx.android.synthetic.main.text_item.view.*

/**
 *Created by ed on 1/24/18.
 */
class FeedRVAdapter(private val steemDiscussion: List<SteemDiscussion>) :
        RecyclerView.Adapter<FeedRVAdapter.FeedViewHolder>() {

    class FeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun setSteemDiscussion(discussion: SteemDiscussion){

            with(discussion){
                itemView.steemTitle.text = title
                itemView.body_text.text = StringUtils.getShorterBody(body)

                Glide.with(itemView.rootView)
                        .load(jsonMetadata.getFirstImgFromJsonMeta())
                        .into(itemView.preview_image)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FeedViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.cardview_steem_post,
                parent, false)
        return FeedRVAdapter.FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return steemDiscussion.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder?, position: Int) {
        val discussion = steemDiscussion[position]
        holder?.setSteemDiscussion(discussion)
    }
}