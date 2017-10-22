package com.taskail.mixion.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.taskail.mixion.DiscussionDetailsActivity;
import com.taskail.mixion.R;
import com.taskail.mixion.RetrofitClient;
import com.taskail.mixion.SteemAPI;
import com.taskail.mixion.adapters.DiscussionsAdapter;
import com.taskail.mixion.models.SteemDiscussion;
import com.taskail.mixion.utils.BottomNavigationViewVisibility;
import com.taskail.mixion.utils.EndlessRecyclerViewScrollListener;
import com.taskail.mixion.utils.FragmentLifecycle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**Created by ed on 9/30/17.
 */

public class FeedFragment extends Fragment implements FragmentLifecycle, DiscussionsAdapter.CardClickListener {
    private static final String TAG = "FeedFragment";

    private BottomNavigationViewVisibility navigationViewVisibility;

    private static final String BASE_URL = "https://api.steemjs.com/";

    SteemAPI steemApi = RetrofitClient.getRetrofitClient(BASE_URL).create(SteemAPI.class);
    private CompositeDisposable disposable = new CompositeDisposable();

    private List<SteemDiscussion> discussionFromResponse = new ArrayList<>();
    private DiscussionsAdapter mAdapter;
    private Spinner typeSpinner, topicsSpinner;
    private String tag;
    private String sortBy;
    private boolean isLoading = false;
    private boolean isPaginated = false;
    private final int loadCount = 10;
    private int beginToLoadAt;
    private CircleProgressView mCirProg;
    private ProgressBar loadMoreProgress;
    private EndlessRecyclerViewScrollListener scrollListener;
    private boolean isVisible = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typeSpinner = view.findViewById(R.id.feed_type);
        topicsSpinner = view.findViewById(R.id.topics);
        mCirProg = view.findViewById(R.id.circleProgress);
        loadMoreProgress = view.findViewById(R.id.load_moreProgress);

        ImageView toolbarImage = view.findViewById(R.id.logo);
        Glide.with(this).asDrawable().load(R.drawable.steem_logo).into(toolbarImage);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter = new DiscussionsAdapter();
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

                    Log.d(TAG, "scrollAction: hiding ");
                    isVisible = false;
                    navigationViewVisibility.hideBNV();
                } else if (dy < 0 && !isVisible){

                    Log.d(TAG, "scrollAction: showing ");
                    isVisible = true;
                    navigationViewVisibility.showBNV();
                }

                //hide the FAB

            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        setupSpinners();
    }


    private void setupSpinners() {
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.feed_type, android.R.layout.simple_spinner_item);
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

        ArrayAdapter<CharSequence> topicsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.topics, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topicsSpinner.setAdapter(topicsAdapter);
        topicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tag = getActivity().getResources().getStringArray(R.array.topics)[i];
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
            startLoadingProgress();

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
                    fetchMoreTrending(discussionFromResponse.get(lastPostLocation-1).getAuthor(), discussionFromResponse.get(lastPostLocation-1).getPermlink());
                    break;
                case ("Hot"):
                    fetchMoreHot(discussionFromResponse.get(lastPostLocation-1).getAuthor(), discussionFromResponse.get(lastPostLocation-1).getPermlink());
                    break;
                case ("New"):
                    fetchMoreNew(discussionFromResponse.get(lastPostLocation-1).getAuthor(), discussionFromResponse.get(lastPostLocation-1).getPermlink());
                    break;
                case ("Promoted"):
                    fetchMorePromoted(discussionFromResponse.get(lastPostLocation-1).getAuthor(), discussionFromResponse.get(lastPostLocation-1).getPermlink());
            }

        }

    }

    private void fetchTrending(){

        disposable.add(steemApi.getRXTrendingDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchHot(){

        disposable.add(steemApi.getRxHotDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchNew(){

        disposable.add(steemApi.getRxNewestDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchPromoted(){

        disposable.add(steemApi.getrxPromotedDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchMoreTrending(String startAuthor,String startPermLink){

        disposable.add(steemApi.getRXTrendingDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));

    }

    private void fetchMoreHot(String startAuthor,String startPermLink){

        disposable.add(steemApi.getRxHotDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchMoreNew(String startAuthor,String startPermLink){

        disposable.add(steemApi.getRxNewestDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void fetchMorePromoted(String startAuthor,String startPermLink){

        disposable.add(steemApi.getrxPromotedDiscussions("{\"tag\":" + "\"" + tag + "\"" + ",\"limit\":\"" + loadCount + "\", \"start_author\":\"" + startAuthor + "\", \"start_permlink\":\"" + startPermLink + "\"}")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(SteemDiscussion[] steem) {

        if (!isPaginated) {
            discussionFromResponse = new ArrayList<>();
            Collections.addAll(discussionFromResponse, steem);
            stopLoadingProgress();
        }else {
            Collections.addAll(discussionFromResponse, Arrays.copyOfRange(steem, 1, loadCount));
            mAdapter.notifyItemRangeInserted(beginToLoadAt, discussionFromResponse.size());
            stopLoadingMore();

        }
        isLoading = false;


        mAdapter.setDiscussion(discussionFromResponse, FeedFragment.this, this);

    }

    private void handleError(Throwable error) {

        Log.d(TAG, "handleError: " + error.getMessage());

        if (error.getMessage().equals("timeout")){
            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
        }

        setOnFailure();
    }

    private void startLoadingProgress(){
        mCirProg.setVisibility(View.VISIBLE);
        mCirProg.setValue(50);
        mCirProg.setText("Loading..");
        mCirProg.spin();
    }
    private void stopLoadingProgress(){
        mCirProg.stopSpinning();
        mCirProg.setVisibility(View.GONE);
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
            mCirProg.stopSpinning();
            mCirProg.setBarColor(Color.RED);
            mCirProg.setText("Retry");
            mCirProg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isLoading) {
                        requestToLoadNew();
                    }
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
        Log.d(TAG, "onPause: Paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getItemCount() <= 0) {
            Log.d(TAG, "onResume: Loading new");
            requestToLoadNew();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: destroyed");
        Glide.get(getActivity()).clearMemory();
        disposable.dispose();
    }
    @Override
    public void onPauseFragment() {
    }
    @Override
    public void onResumeFragment() {
    }

    @Override
    public void onCardClicked(int position) {
        Bundle discussionBundle = new Bundle();
        discussionBundle.putSerializable("data", discussionFromResponse.get(position));
        Intent intent = new Intent(getActivity(), DiscussionDetailsActivity.class);
        intent.putExtras(discussionBundle);
        getActivity().startActivity(intent);
        
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        navigationViewVisibility = (BottomNavigationViewVisibility) getActivity();

    }
}
