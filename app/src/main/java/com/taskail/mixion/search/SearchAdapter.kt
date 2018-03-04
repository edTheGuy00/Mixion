package com.taskail.mixion.search

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskail.mixion.R
import com.taskail.mixion.data.models.Result
import com.taskail.mixion.utils.GetTimeAgo
import kotlinx.android.synthetic.main.item_search_result.view.*

/**
 *Created by ed on 1/29/18.
 */
class SearchAdapter(private val results: List<Result>,
                    private val callback: SearchAdapterCallback) :
        RecyclerView.Adapter<SearchAdapter.ResultViewHolder>() {

    class ResultViewHolder(itemView: View, private val callback: SearchAdapterCallback) : RecyclerView.ViewHolder(itemView) {

        fun setResult(result: Result){

            with(result){
                itemView.title.text = title
                itemView.summary.text = summary
                itemView.votes_count.text = netVotes.toString()
                itemView.replies_count.text = children.toString()
                itemView.time_ago.text = GetTimeAgo.getlongtoago(created)
                itemView.author.text = author

                itemView.setOnClickListener {
                    callback.onItemSelected(author, permlink)
                }

            }
        }
    }

    interface SearchAdapterCallback{
        fun onItemSelected(author: String, permlink: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result,
                parent, false)
        return SearchAdapter.ResultViewHolder(itemView, callback)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.setResult(results[position])
    }
}