package com.taskail.mixion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.taskail.mixion.DiscussionDetailsActivity;
import com.taskail.mixion.R;
import com.taskail.mixion.RetrofitClient;
import com.taskail.mixion.SteemAPI;
import com.taskail.mixion.adapters.AskSteemAdapter;
import com.taskail.mixion.models.AskSteem;
import com.taskail.mixion.models.Result;
import com.taskail.mixion.utils.FragmentLifecycle;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by ed on 9/30/17.
 */

public class AskSteemFragment extends Fragment implements FragmentLifecycle, AskSteemAdapter.ItemClickedListener {
    private static final String TAG = "AskSteemFragmet";

    private static final String ASK_STEEM_URL = "https://api.asksteem.com/";
    private List<Result> resultsFromResponse = new ArrayList<>();
    SteemAPI steemApi = RetrofitClient.getRetrofitClient(ASK_STEEM_URL).create(SteemAPI.class);
    private CompositeDisposable disposable = new CompositeDisposable();
    private AskSteemAdapter mAdapter;
    private boolean isLoading = false;
    private CircleProgressView mCirProg;
    private ScrollView scrollView;
    private RelativeLayout seachRelativeLayout;
    private TextView resultsText;
    private Button nextbtn, prevBtn;
    private int currentPage;
    private int nextPage;
    private int prevPage;
    private String searchFor;
    private Boolean hasMore, hasPrev;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asksteem, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCirProg = view.findViewById(R.id.circleProgress);
        ImageView askSteemImage = view.findViewById(R.id.askseem_logo);
        Glide.with(this).asDrawable().load(R.drawable.ask_steem_logo).into(askSteemImage);

        scrollView = view.findViewById(R.id.askScrollView);
        seachRelativeLayout = view.findViewById(R.id.search_bar);
        resultsText = view.findViewById(R.id.results_text);
        nextbtn = view.findViewById(R.id.next_button);
        nextbtn.setOnClickListener((View v) ->{

            if (hasMore) {
                resultsFromResponse.clear();
                mAdapter.notifyDataSetChanged();
                startLoadingProgress();
                getMorePages(searchFor, nextPage);
            }

        });
        prevBtn = view.findViewById(R.id.previous_button);
        prevBtn.setOnClickListener((View v) ->{

            if (hasPrev) {
                resultsFromResponse.clear();
                mAdapter.notifyDataSetChanged();
                startLoadingProgress();
                getMorePages(searchFor, prevPage);
            }

        });

        RecyclerView recyclerView = view.findViewById(R.id.results_recyclerview);
        mAdapter = new AskSteemAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);

        final EditText searchTerm = view.findViewById(R.id.search_edit_text);
        ImageView searchIcon = view.findViewById(R.id.search_icon);

        searchTerm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){

                    resultsFromResponse.clear();
                    mAdapter.notifyDataSetChanged();
                    startLoadingProgress();
                    askSteem(searchTerm.getText().toString());
                    searchFor = searchTerm.getText().toString();

                    //Hide Keyboard
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });

        searchIcon.setOnClickListener((View) -> {
            if (!TextUtils.isEmpty(searchTerm.getText())){
                resultsFromResponse.clear();
                mAdapter.notifyDataSetChanged();
                startLoadingProgress();
                askSteem(searchTerm.getText().toString());
                searchFor = searchTerm.getText().toString();

                //Hide Keyboard
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        });
    }

    private void askSteem(String term){

        disposable.add(steemApi.searchAskSteem("search+" + term)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void getMorePages(String term, Integer page){

        Log.d(TAG, "getMorePages: " + page);

        disposable.add(steemApi.AskMoreSteem("search+" + term, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(AskSteem askResult) {
        resultsFromResponse = new ArrayList<>();
        if (askResult != null){
            resultsText.setText(String.valueOf(askResult.getHits()) + " results " + "(" + String.valueOf(askResult.getTime()) + " seconds)");
            currentPage = askResult.getPages().getCurrent();
            resultsFromResponse.addAll( askResult.getResults() );
            mAdapter.setResults(resultsFromResponse, this);
            stopLoadingProgress();

            Log.d(TAG, "handleResponse: " + currentPage);

            scrollView.post(() ->{
                scrollView.smoothScrollTo(0, seachRelativeLayout.getTop());
            });

            if (askResult.getPages().getHasNext()){
                nextbtn.setVisibility(View.VISIBLE);
                hasMore = askResult.getPages().getHasNext();
                nextPage = currentPage;
                nextPage++;

                Log.d(TAG, "handleResponse: nextPage " + nextPage);
            } else {
                nextbtn.setVisibility(View.GONE);
            }
            if (askResult.getPages().getHasPrevious()){
                prevBtn.setVisibility(View.VISIBLE);
                hasPrev = askResult.getPages().getHasPrevious();
                prevPage = currentPage;
                prevPage--;

                Log.d(TAG, "handleResponse: " + prevPage);
            } else {
                prevBtn.setVisibility(View.GONE);
            }
        }
    }

    private void handleError(Throwable error) {
        if (error.getMessage().equals("timeout")){
            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
        }
        stopLoadingProgress();
    }

    private void startLoadingProgress(){
        mCirProg.setVisibility(View.VISIBLE);
        mCirProg.setValue(50);
        mCirProg.spin();
    }
    private void stopLoadingProgress(){
        mCirProg.stopSpinning();
        mCirProg.setVisibility(View.GONE);
    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), DiscussionDetailsActivity.class);

        intent.putExtra("Author", resultsFromResponse.get(position).getAuthor());
        intent.putExtra("link", resultsFromResponse.get(position).getPermlink());

        getActivity().startActivity(intent);

        Log.d(TAG, "onItemClick: " + resultsFromResponse.get(position).getAuthor());

    }
}
