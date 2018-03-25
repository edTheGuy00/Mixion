package com.taskail.mixion.fragment

import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.source.local.Drafts
import kotlinx.android.synthetic.main.item_draft.view.*

/**
 *Created by ed on 1/26/18.
 */
class DraftsAdapter(drafts: List<Drafts>,
                    private val openDraft: (Drafts) -> Unit) :

        Adapter<DraftsAdapter.ItemVH>() {

    var drafts: List<Drafts> = drafts

    set(loadedTags) {
        field = loadedTags
        notifyDataSetChanged()
    }

    class ItemVH(itemView: View) :
            ViewHolder(itemView){

        fun setItem(drafts: Drafts,
                    openDraft: (Drafts) -> Unit){

            with(itemView) {
                title.text = drafts.title
                summary.text = drafts.body
                setOnClickListener {
                    openDraft(drafts)
                }
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_draft,
                        parent, false)

        return DraftsAdapter.ItemVH(itemView)
    }

    override fun getItemCount(): Int {
        return drafts.size
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.setItem(drafts[position], openDraft)
    }
}