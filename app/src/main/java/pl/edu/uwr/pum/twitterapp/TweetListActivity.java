package pl.edu.uwr.pum.twitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public class TweetListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton AddTweetFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_list);

        recyclerView = findViewById(R.id.recycler_view_tweet_list);
        AddTweetFAB = findViewById(R.id.AddTweetFAB);

        TweetAdapter tweetAdapter = new TweetAdapter(this, new ArrayList<Tweet>());
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // pobieranie twweetów z "homeTimeline"
        TwitterCore.getInstance().getApiClient().getStatusesService().homeTimeline(null, null, null, null, null, null, null).enqueue(new Callback<List<Tweet>>() {
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

        AddTweetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddingTweetActivity.class);
                startActivity(intent);
            }
        });
    }
}