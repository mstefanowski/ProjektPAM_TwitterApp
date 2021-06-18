package pl.edu.uwr.pum.twitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.twitter.sdk.android.core.TwitterUtils;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class TweetListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton AddTweetFAB;
    static final int UPDATE_TWEETS = 100;
    static final int UPDATE_SETTINGS = 69;
    TweetAdapter tweetAdapter;

    String currentBio;

    TextView currentUserNameTextView;
    TextView currentUserNickNameTextView;

    ImageView currentUserAvatar;
    ToggleButton modeToggleButton;
    Button editButton;

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
        editButton = findViewById(R.id.editButton);

        tweetAdapter = new TweetAdapter(this, new ArrayList<Tweet>());
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        updateSettings();
        updateTweets();


        AddTweetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TweetListActivity.this, AddingTweetActivity.class);
                startActivityForResult(intent, UPDATE_TWEETS);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TweetListActivity.this, SettingsPopUp.class);
                intent.putExtra("currentName", currentUserNameTextView.getText().toString());
                intent.putExtra("currentBio", currentBio);
                startActivityForResult(intent, UPDATE_SETTINGS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_TWEETS){
            updateTweets();
        }
        if(requestCode == UPDATE_SETTINGS) {
            String newBio = data.getStringExtra("newBio");
            String newName = data.getStringExtra("newName");

            Log.i("STEFAN", "newBio= "+newBio);
            Log.i("STEFAN", "newName= " +newName);

            TwitterUtils.getPostAccountService(TwitterCore.getInstance().getApiClient()).postChangedSettings(
                    newName,
                    null,
                    null,
                    newBio,
                    null,
                    null,
                    null).enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    updateSettings();
                }

                @Override
                public void failure(TwitterException exception) {

                }
            });
        }

    }


    private void updateSettings(){
        TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(
                                    false, false, false).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                currentBio = result.data.description;
                currentUserNameTextView.setText(result.data.name);
                currentUserNickNameTextView.setText(result.data.screenName);
                String uri = result.data.profileImageUrlHttps;
                Picasso.get().load(uri).into(currentUserAvatar);

                currentUsernameID = result.data.id;
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
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