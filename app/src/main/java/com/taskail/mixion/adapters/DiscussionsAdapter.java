package com.taskail.mixion.adapters;

import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.taskail.mixion.R;
import com.taskail.mixion.models.SteemDiscussion;
import com.taskail.mixion.utils.StringManipulator;
import com.taskail.mixion.utils.GetTimeAgo;

import java.util.Collections;
import java.util.List;

/**Created by ed on 10/2/17.
 */

public class DiscussionsAdapter extends RecyclerView.Adapter<DiscussionsAdapter.DiscussionItemViewHolder> {

    private static final String TAG = "DiscussionsAdapter";
    private Fragment feedFragment;
    private RequestOptions options = new RequestOptions().error(R.drawable.steem_logo).placeholder(R.drawable.steem_logo);
    private CardClickListener listener;

    private StringManipulator stringManipulator = new StringManipulator();

    private List<SteemDiscussion> discussion = Collections.emptyList();
    public void setDiscussion(List<SteemDiscussion> discussion, Fragment feedFragment, CardClickListener listener){
        this.discussion = discussion;
        this.feedFragment = feedFragment;
        this.listener = listener;
        this.notifyDataSetChanged();
    }

    @Override
    public DiscussionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.card_steem, parent, false);
        return new DiscussionItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiscussionItemViewHolder holder, int position) {
        final SteemDiscussion allDiscussions = this.discussion.get(position);
        holder.setDiscussion(allDiscussions);
        applyOnClick(holder, position);

    }
    @Override
    public int getItemCount() {
        return discussion.size();
    }
    class DiscussionItemViewHolder extends RecyclerView.ViewHolder {
        private TextView body;
        private TextView title;
        private TextView author;
        private TextView category;
        private TextView payout;
        private TextView votesCount;
        private TextView repliesCount;
        private TextView timeAgo;
        private CardView discussionCard;
        private ImageView mainImage;
        DiscussionItemViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.steem_title);
            body = itemView.findViewById(R.id.steem_body);
            author = itemView.findViewById(R.id.author);
            category = itemView.findViewById(R.id.category);
            payout = itemView.findViewById(R.id.payout);
            votesCount = itemView.findViewById(R.id.votes_count);
            repliesCount = itemView.findViewById(R.id.replies_count);
            timeAgo = itemView.findViewById(R.id.time_ago);
            discussionCard = itemView.findViewById(R.id.discussion_card);
            mainImage = itemView.findViewById(R.id.preview_image);
        }
        void setDiscussion(final SteemDiscussion discussion){
            title.setText(discussion.getTitle());
            body.setText(stringManipulator.getFirstFewCharacters(discussion.getBody()));
            author.setText(discussion.getAuthor());
            category.setText(discussion.getCategory());
            payout.setText(discussion.getPendingPayoutValue());
            votesCount.setText(String.valueOf(discussion.getNetVotes()));
            repliesCount.setText(String.valueOf(discussion.getChildren()));
            timeAgo.setText(GetTimeAgo.getlongtoago(discussion.getCreated()));

            Glide.with(feedFragment)
                    .load(stringManipulator.getFirstImageFromJsonMetaData(discussion.getJsonMetadata()))
                    .apply(options)
                    .into(mainImage);

            //TODO - applyOnClick in onBindViewHolder does not work without this.. Why??
            discussionCard.setOnClickListener((View v) -> {
            });
        }

    }

    private void applyOnClick (DiscussionItemViewHolder holder, final int position){

        holder.discussionCard.setOnTouchListener((View view, MotionEvent motionEvent) -> {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ObjectAnimator upAnim = ObjectAnimator.ofFloat(view, "translationZ", 0);
                        upAnim.setDuration(200);
                        upAnim.setInterpolator(new DecelerateInterpolator());
                        upAnim.start();
                        break;
                    case MotionEvent.ACTION_UP:

                        Log.d(TAG, "onTouch: Logging " );
                        listener.onCardClicked(position);

                    case MotionEvent.ACTION_CANCEL:
                        ObjectAnimator downAnim = ObjectAnimator.ofFloat(view, "translationZ", 20);
                        downAnim.setDuration(200);
                        downAnim.setInterpolator(new AccelerateInterpolator());
                        downAnim.start();
                        break;
                }

                return false;

        });
    }

    public interface CardClickListener{
        void onCardClicked(int position);
    }
}