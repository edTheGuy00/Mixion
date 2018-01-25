package com.taskail.mixion.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.taskail.mixion.steempost.DiscussionDetailsActivity;
import com.taskail.mixion.R;
import com.taskail.mixion.adapters.DiscussionsRecyclerAdapter;
import com.taskail.mixion.data.network.RetrofitClient;
import com.taskail.mixion.data.source.SteemAPI;
import com.taskail.mixion.helpers.CircleProgressViewHelper;
import com.taskail.mixion.data.models.SteemDiscussion;
import com.taskail.mixion.utils.Constants;
import com.taskail.mixion.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**Created by ed on 9/30/17.
 *
 * Feed fragment makes all the calls to api.steem.com
 */

public class OldFeedFragment extends Fragment implements DiscussionsRecyclerAdapter.CardClickListener {
    private static final String TAG = "OldFeedFragment";

    SteemAPI steemApi = RetrofitClient.getRetrofitClient(Constants.BASE_URL).create(SteemAPI.class);
    private List<SteemDiscussion> discussionFromResponse = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private EndlessRecyclerViewScrollListener scrollListener;
    private Spinner typeSpinner, topicsSpinner;
    private DiscussionsRecyclerAdapter mAdapter;
    private CircleProgressView circleProgressView;
    private ProgressBar loadMoreProgress;
    private RecyclerView recyclerView;

    private boolean isLoading = false;
    private boolean isPaginated = false;
    private final int loadCount = 10;
    private boolean isVisible = true;
    private int beginToLoadAt;
    private String tag;
    private String sortBy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.old_fragment_feed, container, false);
        typeSpinner = view.findViewById(R.id.feed_type);
        topicsSpinner = view.findViewById(R.id.topics);
        circleProgressView = view.findViewById(R.id.circleProgress);
        loadMoreProgress = view.findViewById(R.id.load_moreProgress);
        recyclerView = view.findViewById(R.id.recycler_view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        ImageView toolbarImage = view.findViewById(R.id.logo);
        Glide.with(this).asDrawable().load(R.drawable.steem_logo).into(toolbarImage);

        mAdapter = new DiscussionsRecyclerAdapter(discussionFromResponse, getActivity(), this);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                beginToLoadAt = totalItemsCount;
                loadMore(totalItemsCount);
            }
            @Override
            public void scrollAction(int dx, int dy) {
                if (dy > 0 && isVisible){
                    isVisible = false;
                } else if (dy < 0 && !isVisible){
                    isVisible = true;
                }

            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        setupSpinners();
    }

    /**
     * Setup the drop down spinners, one is the feed type (hot, new, trending), and the other is
     * the tags available. Array list is found in R.array
     */
    private void setupSpinners() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.feed_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortBy = getActivity().getResources().getStringArray(R.array.feed_type)[i];
                if (!isLoading) {
                    requestToLoadNew();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> topicsAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.tags, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topicsSpinner.setAdapter(topicsAdapter);
        topicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tag = getActivity().getResources().getStringArray(R.array.tags)[i];
                if (!isLoading) {
                    requestToLoadNew();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void requestToLoadNew(){
        if (tag != null) {
            discussionFromResponse.clear();
            mAdapter.notifyDataSetChanged();
            scrollListener.resetState();
            isLoading = true;
            isPaginated = false;
            CircleProgressViewHelper.showLoading(circleProgressView);

            switch (sortBy) {
                case ("Trending"):
                    fetchTrending();
                    break;
                case ("Hot"):
                    fetchHot();
                    break;
                case ("New"):
                    fetchNew();
                    break;
                case ("Promoted"):
                    fetchPromoted();
            }
        }
    }
    private void loadMore(int lastPostLocation){
        isPaginated = true;
        startLoadingMore();

        if (tag != null){

            switch (sortBy) {
                case ("Trending"):
                    fetchMoreTrending(discussionFromResponse.get(lastPostLocation-1).getAuthor(),
                            discussionFromResponse.get(lastPostLocation-1).getPermlink());
                    break;
                case ("Hot"):
                    fetchMoreHot(discussionFromResponse.get(lastPostLocation-1).getAuthor(),
                            discussionFromResponse.get(lastPostLocation-1).getPermlink());
                    break;
                case ("New"):
                    fetchMoreNew(discussionFromResponse.get(lastPostLocation-1).getAuthor(),
                            discussionFromResponse.get(lastPostLocation-1).getPermlink());
                    break;
                case ("Promoted"):
                    fetchMorePromoted(discussionFromResponse.get(lastPostLocation-1).getAuthor(),
                            discussionFromResponse.get(lastPostLocation-1).getPermlink());
            }

        }

    }

    private void fetchTrending(){

        disposable.add(steemApi.getTrendingDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchHot(){

        disposable.add(steemApi.getHotDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchNew(){

        disposable.add(steemApi.getNewestDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchPromoted(){

        disposable.add(steemApi.getPromotedDiscussions("{\"tag\":" + "\"" + tag + "\""
                + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchMoreTrending(String startAuthor,String startPermLink){

        disposable.add(steemApi.getTrendingDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\""
                + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));

    }

    private void fetchMoreHot(String startAuthor,String startPermLink){

        disposable.add(steemApi.getHotDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\""
                + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchMoreNew(String startAuthor,String startPermLink){

        disposable.add(steemApi.getNewestDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\""
                + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchMorePromoted(String startAuthor,String startPermLink){

        disposable.add(steemApi.getPromotedDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\""
                + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(SteemDiscussion[] steem) {

        if (!isPaginated) {
            Collections.addAll(discussionFromResponse, steem);
            CircleProgressViewHelper.stopLoading(circleProgressView);
            mAdapter.notifyDataSetChanged();
        }else {
            Collections.addAll(discussionFromResponse, Arrays.copyOfRange(steem, 1, loadCount));
            mAdapter.notifyItemRangeInserted(beginToLoadAt, discussionFromResponse.size());
            stopLoadingMore();

        }
        isLoading = false;

    }

    private void handleError(Throwable error) {

        Log.d(TAG, "handleError: " + error.getMessage());

        if (error.getMessage().equals("timeout")){
            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
        }

        setOnFailure();
    }

    private void startLoadingMore(){
        loadMoreProgress.setVisibility(View.VISIBLE);
    }
    private void stopLoadingMore(){
        loadMoreProgress.setVisibility(View.GONE);

    }
    private void setOnFailure(){
        isLoading = false;
        if (!isPaginated) {
            CircleProgressViewHelper.setRetryError(circleProgressView);
            circleProgressView.setOnClickListener((View view) -> {
                if (!isLoading) {
                    requestToLoadNew();
                }
            });
        } else {
            stopLoadingMore();
            Toast.makeText(getActivity(), "Unable to Load More", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // save RecyclerView state
        
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: destroyed");
        Glide.get(getActivity()).clearMemory();
        disposable.dispose();
    }

    @Override
    public void onCardClicked(int position) {
        Bundle discussionBundle = new Bundle();
        discussionBundle.putSerializable("data", discussionFromResponse.get(position));
        Intent intent = new Intent(getActivity(), DiscussionDetailsActivity.class);
        intent.putExtras(discussionBundle);
        getActivity().startActivity(intent);
        
    }

}
