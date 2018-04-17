package com.taskail.mixion.fragment

import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.Drafts
import kotlinx.android.synthetic.main.item_draft.view.*

/**
 *Created by ed on 1/26/18.
 */
class DraftsAdapter(drafts: MutableList<Drafts>,
                    private val openDraft: (Drafts) -> Unit,
                    private val deleteDraft: (String, Int) -> Unit) :

        Adapter<DraftsAdapter.ItemVH>() {

    var drafts: MutableList<Drafts> = drafts

    set(loadedTags) {
        field = loadedTags
        notifyDataSetChanged()
    }

    class ItemVH(itemView: View) :
            ViewHolder(itemView){

        fun setItem(draft: Drafts,
                    position: Int,
                    openDraft: (Drafts) -> Unit,
                    deleteDraft: (String, Int) -> Unit){

            with(itemView) {
                title.text = draft.title
                summary.text = draft.body
                setOnClickListener {
                    openDraft(draft)
                }

                setOnLongClickListener {
                    deleteDraft(draft.id, position)
                    return@setOnLongClickListener true
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
        holder.setItem(drafts[position], position, openDraft, deleteDraft)
    }
}