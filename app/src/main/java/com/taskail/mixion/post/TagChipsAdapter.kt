package com.taskail.mixion.post

import android.support.v7.widget.RecyclerView
import android.util.Log
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

    var tags = mutableListOf<String>()

    class TagsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun setTag(tag: String, position: Int, remove: (Int) -> Unit){
            itemView.chip_tag.text = tag
            itemView.chip_action.setImageResource(R.drawable.ic_trashcan_delete_12dp)

            itemView.setOnClickListener {
                Log.d("Chip Adapter", "Delete this tag")
                remove(position.minus(1))
            }
        }

        fun setAddNewTag(add: (String) -> Unit) {
            itemView.chip_tag.setText(R.string.add_tags)
            itemView.chip_action.setImageResource(R.drawable.ic_add_12dp)

            itemView.setOnClickListener {
                add("steemit")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TagsViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.layout_chip_tags,
                        parent, false)

        return TagsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TagsViewHolder?, position: Int) {
        when (position){
            0 -> holder?.setAddNewTag(
                    {
                        if (tags.size < 5){
                        tags.add(it)
                                .also { this.notifyDataSetChanged() }
                        } else {
                            Log.d("Chip recycler", "reached limit")
                        }
                    })
            else -> holder?.setTag(tags[position.minus(1)],
                    position,
                    {
                        tags.removeAt(it)
                                .also { this.notifyDataSetChanged() }
                    })
        }
    }

    override fun getItemCount(): Int {
        var count = 1
        count += tags.size
        return count
    }
}