package pl.edu.uwr.pum.twitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class TweetListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton AddTweetFAB;
    static final int UPDATE_TWEETS = 100;
    TweetAdapter tweetAdapter;

    TextView currentUserNameTextView;
    TextView currentUserNickNameTextView;

    ImageView currentUserAvatar;
    ToggleButton modeToggleButton;

    long currentUsernameID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);

        recyclerView = findViewById(R.id.recycler_view_tweet_list);
        AddTweetFAB = findViewById(R.id.AddTweetFAB);

        currentUserNameTextView = findViewById(R.id.currentUserNameTextView);
        currentUserNickNameTextView = findViewById(R.id.currentUserNicknameTextView);
        currentUserAvatar = findViewById(R.id.currentUserAvatar);

        modeToggleButton = findViewById(R.id.modeToggleButton);

        tweetAdapter = new TweetAdapter(this, new ArrayList<Tweet>());
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false, false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                currentUserNameTextView.setText(result.data.screenName);
                currentUserNickNameTextView.setText(result.data.name);
                String uri = result.data.profileImageUrlHttps;
                Picasso.get().load(uri).into(currentUserAvatar);

                currentUsernameID = result.data.id;
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });


        updateTweets();


        AddTweetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddingTweetActivity.class);
                startActivityForResult(intent, UPDATE_TWEETS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_TWEETS){
            updateTweets();
        }
    }

    private void updateTweets(){
        if(!modeToggleButton.isChecked()){
            // pobieranie twweetów z "homeTimeline"
            TwitterCore.getInstance().getApiClient().getStatusesService().homeTimeline(
                    50,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null).enqueue(new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    tweetAdapter.setTweetList(result.data);
                    tweetAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.i("stefan", exception.toString());
                }
            });
        } else {
            TwitterCore.getInstance().getApiClient().getStatusesService().userTimeline(currentUsernameID,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null).enqueue(new Callback<List<Tweet>>() {
                @Override
                public void success(Result<List<Tweet>> result) {
                    tweetAdapter.setTweetList(result.data);
                    tweetAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.i("stefan", exception.toString());
                }
            });
        }
    }

    public void modeToggleClicked(View view) {
        updateTweets();
    }
}