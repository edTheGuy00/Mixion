package com.taskail.mixion.post

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.taskail.mixion.R
import com.taskail.mixion.ui.dialog.EnterTagDialog
import com.taskail.mixion.post.TagChipsAdapter.TagsViewHolder
import kotlinx.android.synthetic.main.layout_chip_tags.view.*

/**
 *Created by ed on 2/16/18.
 */

class TagChipsAdapter(private val context: Context) : RecyclerView.Adapter<TagsViewHolder>() {

    val TAG = javaClass.simpleName

    var tags = mutableListOf<String>()

    inner class TagsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun setTag(tag: String, position: Int, remove: (Int) -> Unit){
            itemView.chip_tag.text = tag
            itemView.chip_action.setImageResource(R.drawable.ic_trashcan_delete_12dp)

            itemView.setOnClickListener {
                Log.d(TAG, "Delete this tag")
                remove(position.minus(1))
            }
        }

        fun setAddNewTag(addTag: (String) -> Unit) {
            itemView.chip_tag.setText(R.string.add_tags)
            itemView.chip_action.setImageResource(R.drawable.ic_add_12dp)

            itemView.setOnClickListener {
                if (tags.size < 5)
                    showDialog(addTag)
                else
                    Toast.makeText(context, "Only 5 Tags Allowed", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showDialog(add: (String) -> Unit){
        EnterTagDialog(context, add)
                .show()

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
                                .also {
                                    this.notifyDataSetChanged()
                                }
                        } else {
                            Log.i(TAG, "reached limit")
                        }
                    })
            else -> holder?.setTag(tags[position.minus(1)],
                    position,
                    {
                        tags.removeAt(it)
                                .also {
                                    this.notifyDataSetChanged()
                                }
                    })
        }
    }

    override fun getItemCount(): Int {
        var count = 1
        count += tags.size
        return count
    }
}