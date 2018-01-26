package com.taskail.mixion.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.taskail.mixion.steempost.DiscussionDetailsActivity;
import com.taskail.mixion.R;
import com.taskail.mixion.data.network.RetrofitClient;
import com.taskail.mixion.data.source.remote.SteemAPI;
import com.taskail.mixion.adapters.AskSteemAdapter;
import com.taskail.mixion.helpers.CircleProgressViewHelper;
import com.taskail.mixion.data.models.AskSteem;
import com.taskail.mixion.data.models.Result;

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

public class AskSteemFragment extends Fragment implements AskSteemAdapter.ItemClickedListener {
    private static final String TAG = "AskSteemFragmet";

    private static final String ASK_STEEM_URL = "https://api.asksteem.com/";
    private List<Result> resultsFromResponse = new ArrayList<>();
    SteemAPI steemApi = RetrofitClient.getRetrofitClient(ASK_STEEM_URL).create(SteemAPI.class);
    private CompositeDisposable disposable = new CompositeDisposable();
    private EditText searchInput;
    private AskSteemAdapter mAdapter;
    private boolean isLoading = false;
    private CircleProgressView circleProgressView;
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
        circleProgressView = view.findViewById(R.id.circleProgress);
        ImageView askSteemImage = view.findViewById(R.id.askseem_logo);
        Glide.with(this).asDrawable().load(R.drawable.ask_steem_logo).into(askSteemImage);

        scrollView = view.findViewById(R.id.askScrollView);
        seachRelativeLayout = view.findViewById(R.id.search_bar);
        resultsText = view.findViewById(R.id.results_text);
        nextbtn = view.findViewById(R.id.next_button);
        nextbtn.setOnClickListener((View v) ->{
            if (hasMore) {
                loadNextPage();
            }
        });
        prevBtn = view.findViewById(R.id.previous_button);
        prevBtn.setOnClickListener((View v) ->{
            if (hasPrev) {
                loadPreviousPage();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.results_recyclerview);
        mAdapter = new AskSteemAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);

        searchInput = view.findViewById(R.id.search_edit_text);
        ImageView searchIcon = view.findViewById(R.id.search_icon);
        searchInput.setOnEditorActionListener((TextView textView, int i, KeyEvent keyEvent) ->{
            if ( i == KeyEvent.KEYCODE_CALL ){
                performSearch(getSearchInput(), view);
                return true;
            }

            return false;
        });

        searchIcon.setOnClickListener((View) -> performSearch(getSearchInput(), view));
    }

    private String getSearchInput() throws NullPointerException{
        return searchInput.getText().toString();
    }

    private void performSearch(String term, View v){
        resultsFromResponse.clear();
        mAdapter.notifyDataSetChanged();
        CircleProgressViewHelper.showLoading(circleProgressView);
        askSteem(term);
        searchFor = term;
        //Hide Keyboard
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        if (in != null) {
            in.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void loadNextPage(){
        resultsFromResponse.clear();
        mAdapter.notifyDataSetChanged();
        CircleProgressViewHelper.showLoading(circleProgressView);
        getMorePages(searchFor, nextPage);
    }

    private void loadPreviousPage(){
        resultsFromResponse.clear();
        mAdapter.notifyDataSetChanged();
        CircleProgressViewHelper.showLoading(circleProgressView);
        getMorePages(searchFor, prevPage);
    }

    private void askSteem(String term){

        disposable.add(steemApi.searchAskSteem("search+" + term)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void getMorePages(String term, Integer page){

        disposable.add(steemApi.AskMoreSteem("search+" + term, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(AskSteem askResult) {
        resultsFromResponse = new ArrayList<>();
        if (askResult != null){
            StringBuilder results = new StringBuilder();
            results.append(String.valueOf(askResult.getHits())).append(" results ").append(String.valueOf(askResult.getTime())).append(" seconds ");
            resultsText.setText(results);
            currentPage = askResult.getPages().getCurrent();
            resultsFromResponse.addAll( askResult.getResults() );
            mAdapter.setResults(resultsFromResponse, this);
            CircleProgressViewHelper.stopLoading(circleProgressView);
            scrollView.post(() -> scrollView.smoothScrollTo(0, seachRelativeLayout.getTop()));

            if (askResult.getPages().getHasNext()){
                nextbtn.setVisibility(View.VISIBLE);
                hasMore = askResult.getPages().getHasNext();
                nextPage = currentPage;
                nextPage++;
            } else {
                nextbtn.setVisibility(View.GONE);
            }
            if (askResult.getPages().getHasPrevious()){
                prevBtn.setVisibility(View.VISIBLE);
                hasPrev = askResult.getPages().getHasPrevious();
                prevPage = currentPage;
                prevPage--;
            } else {
                prevBtn.setVisibility(View.GONE);
            }
        }
    }

    private void handleError(Throwable error) {
        if (error.getMessage().equals("timeout")){
            Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
        }
        CircleProgressViewHelper.stopLoading(circleProgressView);
    }


    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(getActivity(), DiscussionDetailsActivity.class);
        intent.putExtra("Author", resultsFromResponse.get(position).getAuthor());
        intent.putExtra("link", resultsFromResponse.get(position).getPermlink());
        getActivity().startActivity(intent);

    }
}
