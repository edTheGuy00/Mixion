package com.taskail.mixion.post

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.post.TagChipsAdapter.TagsViewHolder
import kotlinx.android.synthetic.main.layout_chip_tags.view.*

/**
 *Created by ed on 2/16/18.
 */

class TagChipsAdapter : RecyclerView.Adapter<TagsViewHolder>() {

    var tags = mutableListOf("Tag")

    class TagsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun setTag(tag: String){
            itemView.chip_tag.text = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TagsViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_chip_tags,
                        parent, false)

        return TagsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TagsViewHolder?, position: Int) {
        holder?.setTag(tags[position])
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}