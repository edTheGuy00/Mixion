package com.taskail.mixion.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.taskail.mixion.R;
import com.taskail.mixion.models.FullDiscussion;

import java.util.List;

/**
 * Created by ed on 10/7/17.
 */

public class DiscussionDetailsAdapter extends RecyclerView.Adapter<DiscussionDetailsAdapter.ViewHolder>{
    private List<FullDiscussion> data;
    private Context mContext;
    private RequestOptions options = new RequestOptions().error(R.drawable.steem_logo).placeholder(R.drawable.steem_logo);

    private static final String TAG = "discuAdapter";

    static final int TYPE_TEXT = 0;
    static final int TYPE_IMAGE = 1;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView bodyText;
        public ImageView bodyImage;

        public ViewHolder (View itemView){
            super(itemView);

            bodyText = itemView.findViewById(R.id.body_text);
            bodyImage = itemView.findViewById(R.id.obdy_image);

        }
    }

    public DiscussionDetailsAdapter(Context mContext, List<FullDiscussion> data){
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {

        Log.d(TAG, "getItemViewType: position " + position);

        if (!data.get(position).isText()){
            return TYPE_IMAGE;
        } else{
            return TYPE_TEXT;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: " + viewType);
        View itemView = null;
        switch (viewType) {
            case TYPE_TEXT: {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent, false);
                return new ViewHolder(itemView);
            }
            case TYPE_IMAGE: {
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
                return new ViewHolder(itemView);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_TEXT:{

                Log.d(TAG, "onBindViewHolder: A Text!!! ");
                TextView text = holder.bodyText;
                text.setText(Html.fromHtml(data.get(position).getmString()));
            }
            break;
            case TYPE_IMAGE: {

                Log.d(TAG, "onBindViewHolder: And Image!! ");
                ImageView image = holder.bodyImage;
                Glide.with(mContext).applyDefaultRequestOptions(options).load(data.get(position).getmString()).into(image);
            }
            break;
        }


    }



    @Override
    public int getItemCount() {
        return data.size();
    }
}
