package com.taskail.mixion.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taskail.mixion.R;
import com.taskail.mixion.models.Result;
import com.taskail.mixion.utils.GetTimeAgo;

import java.util.Collections;
import java.util.List;

/**Created by ed on 10/2/17.
 */

public class AskSteemAdapter extends RecyclerView.Adapter<AskSteemAdapter.ResultsItemViewHolder> {

    private static final String TAG = "AskSteemAdapter";

    private ItemClickedListener listener;

    private List<Result> results = Collections.emptyList();
    public void setResults(List<Result> results, ItemClickedListener listener){
        this.results = results;
        this.notifyDataSetChanged();
        this.listener = listener;
    }

    @Override
    public ResultsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.layout_search_result, parent, false);
        return new ResultsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsItemViewHolder holder, int position) {
        final Result allResults = this.results.get(position);
        holder.setResults(allResults, position);
    }
    @Override
    public int getItemCount() {
        return results.size();
    }
    class ResultsItemViewHolder extends RecyclerView.ViewHolder {
        private TextView summary;
        private TextView title;
        private TextView author;
        private TextView category;
        private TextView votesCount;
        private TextView repliesCount;
        private TextView timeAgo;
        private LinearLayout linearLayout;

        ResultsItemViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.title);
            summary = itemView.findViewById(R.id.summary);
            votesCount = itemView.findViewById(R.id.votes_count);
            repliesCount = itemView.findViewById(R.id.replies_count);
            author = itemView.findViewById(R.id.author);
            timeAgo = itemView.findViewById(R.id.time_ago);
            linearLayout = itemView.findViewById(R.id.result_item);
        }
        void setResults(Result results, int position){
            title.setText(results.getTitle());
            summary.setText(results.getSummary());
            votesCount.setText(String.valueOf(results.getNetVotes()));
            repliesCount.setText(String.valueOf(results.getChildren()));
            author.setText(results.getAuthor());
            timeAgo.setText(GetTimeAgo.getlongtoago(results.getCreated()));

            linearLayout.setOnClickListener((View view) -> listener.onItemClick(position));
        }
    }

    public interface ItemClickedListener{
        void onItemClick(int position);
    }
}