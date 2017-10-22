package com.taskail.mixion;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.bumptech.glide.Glide;
import com.taskail.mixion.models.SteemDiscussion;
import com.taskail.mixion.utils.GetTimeAgo;
import com.taskail.mixion.utils.MixionApolloApplication;
import com.taskail.mixion.utils.StringManipulator;

import at.grabner.circleprogress.CircleProgressView;
import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**Created by ed on 10/6/17.
 */

public class DiscussionDetailsActivity extends AppCompatActivity {
    private static final String TAG = "DiscussionDetailsActivi";

    private static final String BASE_URL = "https://api.steemjs.com/";
    private CompositeDisposable disposable = new CompositeDisposable();
    SteemAPI steemApi = RetrofitClient.getRetrofitClient(BASE_URL).create(SteemAPI.class);

    StringManipulator stringManipulator;
    private RecyclerView mRecyclerView;
    private Context mContext = DiscussionDetailsActivity.this;
    private TextView title, author, category, payout, votesCount, repliesCount, timeAgo;
    private CircleProgressView mCirProg;

    private MixionApolloApplication application;

    private MarkdownView markdownView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_details);
        application = (MixionApolloApplication) getApplication();
        ImageView menu = findViewById(R.id.menu_img);
        initWidgets();
        ImageView toolbarImage = findViewById(R.id.logo);
        Glide.with(this).asDrawable().load(R.drawable.steem_logo).into(toolbarImage);

    }

    private void initWidgets(){
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        category = findViewById(R.id.category);
        payout = findViewById(R.id.payout);
        votesCount = findViewById(R.id.votes_count);
        repliesCount = findViewById(R.id.replies_count);
        timeAgo = findViewById(R.id.time_ago);
        mCirProg = findViewById(R.id.circleProgress);

        markdownView = findViewById(R.id.markdown_web);
        stringManipulator = new StringManipulator();

        Bundle bundle = this.getIntent().getExtras();
        handleBundle(bundle);

    }

    private void handleBundle(Bundle bundle){

        if (bundle != null){

            Log.d(TAG, "handleBundle: bundle not mepty");

            SteemDiscussion steemDiscussion = (SteemDiscussion) bundle.getSerializable("data");
            if (steemDiscussion != null) {

                title.setText(steemDiscussion.getTitle());
                author.setText(steemDiscussion.getAuthor());
                category.setText(steemDiscussion.getCategory());
                payout.setText(steemDiscussion.getPendingPayoutValue());
                votesCount.setText(String.valueOf(steemDiscussion.getNetVotes()));
                repliesCount.setText(String.valueOf(steemDiscussion.getChildren()));
                timeAgo.setText(GetTimeAgo.getlongtoago(steemDiscussion.getCreated()));

                markdownView.addStyleSheet(new Github()).loadMarkdown(Html.fromHtml(steemDiscussion.getBody()).toString());
            } else {
                loadData(bundle.getString("Author"), bundle.getString("link"));

            }
        } else {
            Log.d(TAG, "handleBundle: bundle empty");
        }

    }

    private void loadData(String author, String link){
        startLoadingProgress();

        ApolloCall<GetSingleDiscussionQuery.Data> discussionData = application.apolloClient()
                .query(new GetSingleDiscussionQuery(author, link))
                .responseFetcher(ApolloResponseFetchers.NETWORK_ONLY);

        disposable.add(Rx2Apollo.from(discussionData)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableObserver<Response<GetSingleDiscussionQuery.Data>>() {
            @Override public void onNext(Response<GetSingleDiscussionQuery.Data> dataResponse) {
                handleResponse(dataResponse.data());
            }

            @Override public void onError(Throwable e) {
                firstServerErrorHandler(author, link);
            }

            @Override public void onComplete() {
                stopLoadingProgress();
            }
        }));
    }

    private void handleResponse(GetSingleDiscussionQuery.Data responseData){
        final GetSingleDiscussionQuery.Post post = responseData.post();
        if (post != null){
            title.setText(post.title());
            author.setText(post.author());
            category.setText(post.category());
            payout.setText(post.pending_payout_value());
            votesCount.setText(String.valueOf(post.net_votes()));
            repliesCount.setText(String.valueOf(post.children()));
            timeAgo.setText(GetTimeAgo.getlongtoago(post.created()));
            markdownView.addStyleSheet(new Github()).loadMarkdown(Html.fromHtml(post.body()).toString());
        }
    }

    private void handleSecondResponse(SteemDiscussion steemDiscussion) {

        title.setText(steemDiscussion.getTitle());
        author.setText(steemDiscussion.getAuthor());
        category.setText(steemDiscussion.getCategory());
        payout.setText(steemDiscussion.getPendingPayoutValue());
        votesCount.setText(String.valueOf(steemDiscussion.getNetVotes()));
        repliesCount.setText(String.valueOf(steemDiscussion.getChildren()));
        timeAgo.setText(GetTimeAgo.getlongtoago(steemDiscussion.getCreated()));

        markdownView.addStyleSheet(new Github()).loadMarkdown(Html.fromHtml(steemDiscussion.getBody()).toString());

        stopLoadingProgress();
    }

    private void handleError(Throwable e) {
        setError();
        Log.e(TAG, e.getMessage(), e);
        Toast.makeText(mContext, "Unable to Load", Toast.LENGTH_SHORT).show();
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

    private void firstServerErrorHandler(String author, String link){
        Toast.makeText(mContext, "Server Error, retrying different server", Toast.LENGTH_SHORT).show();

        disposable.add(steemApi.getContent(author, link)
         .observeOn(AndroidSchedulers.mainThread())
         .subscribeOn(Schedulers.io())
         .subscribe(this::handleSecondResponse,this::handleError));
    }

    private void setError(){
        mCirProg.stopSpinning();
        mCirProg.setBarColor(Color.RED);
        mCirProg.setText("ERROR");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null){
            disposable.dispose();
        }
    }
}
