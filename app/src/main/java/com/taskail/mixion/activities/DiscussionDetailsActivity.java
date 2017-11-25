package com.taskail.mixion.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.apollographql.apollo.rx2.Rx2Apollo;
import com.taskail.mixion.GetSingleDiscussionQuery;
import com.taskail.mixion.adapters.CommentsRecyclerAdapter;
import com.taskail.mixion.models.ContentReply;
import com.taskail.mixion.network.MixionApolloClient;
import com.taskail.mixion.R;
import com.taskail.mixion.network.RetrofitClient;
import com.taskail.mixion.helpers.CircleProgressViewHelper;
import com.taskail.mixion.interfaces.SteemAPI;
import com.taskail.mixion.models.ActiveVote;
import com.taskail.mixion.models.SteemDiscussion;
import com.taskail.mixion.utils.Constants;
import com.taskail.mixion.utils.GetTimeAgo;
import com.taskail.mixion.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.styles.Github;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**Created by ed on 10/6/17.
 */

public class DiscussionDetailsActivity extends AppCompatActivity {
    private static final String TAG = "DiscussionDetailsActivi";

    private CompositeDisposable disposable = new CompositeDisposable();
    SteemAPI steemApi = RetrofitClient.getRetrofitClient(Constants.BASE_URL).create(SteemAPI.class);

    StringUtils stringUtils;
    private RecyclerView commentsRecyclerView;
    private Context mContext = DiscussionDetailsActivity.this;
    private TextView titleTV, authorTV, categoryTV, payoutTV, votesCountTV, repliesCountTV, timeAgoTV;
    private CircleProgressView circleProgressView;
    private List<ActiveVote> voters = new ArrayList<>();
    private List<ContentReply> contentRepliesList = new ArrayList<>();
    private CommentsRecyclerAdapter repliesAdapter;
    private ScrollView scrollView;
    private String author, permLink;

    private ApolloClient mApolloClient;
    private MarkdownView markdownView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_details);
        mApolloClient = MixionApolloClient.getApolloCleint();
        commentsRecyclerView = findViewById(R.id.comments_list);
        scrollView = findViewById(R.id.scrollView);
        ImageView menu = findViewById(R.id.menu_img);
        initWidgets();
        initCommentsLayout();
    }

    /**
     *A RecyclerView for the comments(replies) of a particular discussion.
     */
    private void initCommentsLayout(){
        commentsRecyclerView = findViewById(R.id.comments_list);
        commentsRecyclerView.setNestedScrollingEnabled(true);
        repliesAdapter = new CommentsRecyclerAdapter(contentRepliesList, mContext);
        commentsRecyclerView.setAdapter(repliesAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        commentsRecyclerView.setLayoutManager(layoutManager);
    }

    private void initWidgets(){
        titleTV = findViewById(R.id.title);
        authorTV = findViewById(R.id.author);
        categoryTV = findViewById(R.id.category);
        payoutTV = findViewById(R.id.payout);
        votesCountTV = findViewById(R.id.votes_count);
        repliesCountTV = findViewById(R.id.replies_count);
        timeAgoTV = findViewById(R.id.time_ago);
        circleProgressView = findViewById(R.id.circleProgress);

        markdownView = findViewById(R.id.markdown_web);
        stringUtils = new StringUtils();

        Bundle bundle = this.getIntent().getExtras();
        handleBundle(bundle);

    }

    /**
     * @param bundle passed from the calling fragment
     * It will either be a serializable bundle or two strings
     */
    private void handleBundle(Bundle bundle){

        if (bundle != null){

            SteemDiscussion steemDiscussion = (SteemDiscussion) bundle.getSerializable("data");
            if (steemDiscussion != null) {
                if (steemDiscussion.getActiveVotes().size() > 0){
                    voters = steemDiscussion.getActiveVotes();
                }

                author = steemDiscussion.getAuthor();
                permLink = steemDiscussion.getPermlink();

                setTexts(steemDiscussion.getTitle(), steemDiscussion.getAuthor(), steemDiscussion.getCategory(),
                        steemDiscussion.getPendingPayoutValue(),
                        String.valueOf(steemDiscussion.getNetVotes()),
                        String.valueOf(steemDiscussion.getChildren()), steemDiscussion.getCreated(), steemDiscussion.getBody());
            } else {

                author = bundle.getString("Author");
                permLink = bundle.getString("link");
                loadData(author, permLink);

            }
        } else {
            Log.d(TAG, "handleBundle: bundle empty");
        }

    }

    /**
     * The two strings passed from the AskSteem fragmnet are:
     * @param author and
     * @param link are used for querying the particular steem discussion.
     */

    private void loadData(String author, String link){
        CircleProgressViewHelper.showLoading(circleProgressView);

        ApolloCall<GetSingleDiscussionQuery.Data> discussionData = mApolloClient
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
                Log.e(TAG, "onError: " + e.getMessage() );
                firstServerErrorHandler(author, link);
            }

            @Override public void onComplete() {
                CircleProgressViewHelper.stopLoading(circleProgressView);
            }
        }));
    }

    /**
     * Load the response from the GraphQL server
     * @param responseData response containing the date for the discussion
     */
    private void handleResponse(GetSingleDiscussionQuery.Data responseData){
        final GetSingleDiscussionQuery.Post post = responseData.post();
        if (post != null){

            setTexts(post.title(), post.author(), post.category(), post.pending_payout_value(),
                    String.valueOf(post.net_votes()),
                    String.valueOf(post.children()),
                    post.created(),
                    post.body());
        }
    }

    /**
     * The first call is made to @sarasate GrahpQL server, if that fails,
     * a second call will be made to the default api.steemjs.com
     */
    private void firstServerErrorHandler(String author, String link){
        Toast.makeText(mContext, "Server Error, retrying different server", Toast.LENGTH_SHORT).show();

        disposable.add(steemApi.getContent(author, link)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSecondResponse,this::handleError));
    }

    private void handleSecondResponse(SteemDiscussion steemDiscussion) {

        if (steemDiscussion.getActiveVotes().size() > 0){
            voters = steemDiscussion.getActiveVotes();
        }
        setTexts(steemDiscussion.getTitle(), steemDiscussion.getAuthor(), steemDiscussion.getCategory(),
                steemDiscussion.getPendingPayoutValue(),
                String.valueOf(steemDiscussion.getNetVotes()),
                String.valueOf(steemDiscussion.getChildren()), steemDiscussion.getCreated(), steemDiscussion.getBody());
    }

    private void setTexts(String title, String author, String category, String payout, String votes,
                          String replies, String created, String body){
        titleTV.setText(title);
        authorTV.setText(author);
        categoryTV.setText(category);
        payoutTV.setText(payout);
        votesCountTV.setText(votes);
        repliesCountTV.setText(replies);
        timeAgoTV.setText(GetTimeAgo.getlongtoago(created));

        markdownView.addStyleSheet(new Github()).loadMarkdown(Html.fromHtml(body).toString());

        CircleProgressViewHelper.stopLoading(circleProgressView);

    }

    public void loadComments(View view){
        if (!isLoading())
            disposable.add(steemApi.getContentReplies(author, permLink)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError(throwable -> {
                        Log.e(TAG, "loadComments: ", throwable);
                    })
                    .doOnNext(contentReplies -> {
                        Collections.addAll(contentRepliesList, contentReplies);
                        repliesAdapter.notifyDataSetChanged();
                        scrollView.post(() -> scrollView.smoothScrollTo(0, commentsRecyclerView.getTop()));
                    })
                    .subscribe());
    }

    public void showVoters(View view){
        if ( !isLoading() && votersExist())
            Toast.makeText(mContext, "Show Voters " + voters.get(0).getVoter(), Toast.LENGTH_SHORT).show();

    }
    private boolean votersExist(){
        return voters.size() > 0;
    }
    private boolean isLoading(){
        return circleProgressView.getVisibility() == View.VISIBLE;
    }

    private void handleError(Throwable e) {
        CircleProgressViewHelper.unableToLoadError(circleProgressView);
        Log.e(TAG, e.getMessage(), e);
        Toast.makeText(mContext, "Unable to Load", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null){
            disposable.dispose();
        }
    }
}
