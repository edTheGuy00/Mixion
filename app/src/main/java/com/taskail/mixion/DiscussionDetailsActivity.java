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

import com.bumptech.glide.Glide;
import com.taskail.mixion.models.SteemDiscussion;
import com.taskail.mixion.utils.GetTimeAgo;
import com.taskail.mixion.utils.StringManipulator;

import at.grabner.circleprogress.CircleProgressView;
import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**Created by ed on 10/6/17.
 */

public class DiscussionDetailsActivity extends AppCompatActivity {
    private static final String TAG = "DiscussionDetailsActivi";

    private static final String BASE_URL = "https://api.steemjs.com/";
    private CompositeDisposable disposable = new CompositeDisposable();
    SteemAPI steemApi = RetrofitClient.getRetrofitClient(BASE_URL).create(SteemAPI.class);

    private SteemDiscussion steemDiscussion;
    StringManipulator stringManipulator;
    private RecyclerView mRecyclerView;
    private Context mContext = DiscussionDetailsActivity.this;
    private TextView title, author, category, payout, votesCount, repliesCount, timeAgo;
    private CircleProgressView mCirProg;


    private MarkdownView markdownView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_details);
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

            steemDiscussion = (SteemDiscussion) bundle.getSerializable("data");
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

        disposable.add(steemApi.getContent(author, link)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(SteemDiscussion steemDiscussion) {

        title.setText(steemDiscussion.getTitle());
        author.setText(steemDiscussion.getAuthor());
        category.setText(steemDiscussion.getCategory());
        payout.setText(steemDiscussion.getPendingPayoutValue());
        votesCount.setText(String.valueOf(steemDiscussion.getNetVotes()));
        repliesCount.setText(String.valueOf(steemDiscussion.getChildren()));
        timeAgo.setText(GetTimeAgo.getlongtoago(steemDiscussion.getCreated()));

        markdownView.addStyleSheet(new Github()).loadMarkdown(Html.fromHtml(steemDiscussion.getBody()).toString());

        stopLoadingProgress();

        Log.d(TAG, "handleResponse: " + steemDiscussion.getAuthor());

    }

    private void handleError(Throwable throwable) {
        setError();
        Log.d(TAG, "handleError: " + throwable.getMessage());
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
