package com.taskail.mixion.ui.dialog

import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.local.RoomTags
import com.taskail.mixion.ui.dialog.TagDialogAdapter.ItemVH
import com.taskail.mixion.ui.dialog.TagDialog.TagDialogCallback
import kotlinx.android.synthetic.main.items_dialog_tags.view.*

/**
 *Created by ed on 1/26/18.
 */
class TagDialogAdapter(private val callback: TagDialogCallback,
                       tags: List<RoomTags>) :
        Adapter<ItemVH>() {

    var tags: List<RoomTags> = tags

    set(loadedTags) {
        field = loadedTags
        notifyDataSetChanged()
    }

    class ItemVH(itemView: View, val callback: TagDialogCallback) : ViewHolder(itemView){

        fun setItem(tag: RoomTags){

            itemView.text_tag.text = tag.tag
            itemView.text_posts.text = "${tag.posts}"

            itemView.setOnClickListener {
                callback.onTagSelected(tag.tag)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.items_dialog_tags,
                        parent, false)

        return TagDialogAdapter.ItemVH(itemView, callback)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.setItem(tags[position])
    }
}