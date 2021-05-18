package pl.edu.uwr.pum.twitterapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

public class AddingTweetActivity extends AppCompatActivity {

    EditText addTweetEditText;
    Button addTweetButton;
    static final int UPDATE_TWEETS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_tweet);

        addTweetButton = findViewById(R.id.addTweetButton);
        addTweetEditText = findViewById(R.id.AddTweetEditText);



        addTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetText = addTweetEditText.getText().toString();

                if(tweetText.length() < 280) {
                    Log.i("Stefan", "b4 update");
                    TwitterCore.getInstance().getApiClient().getStatusesService().update(tweetText,
                            null, null, null,
                            null, null, null,
                            null , null).enqueue(new Callback<Tweet>() {
                        @Override
                        public void success(Result<Tweet> result) {
                            Log.i("Stefan", "Success update");

                            Intent intent = new Intent(getApplicationContext(), TweetListActivity.class);
                            startActivityForResult(intent, UPDATE_TWEETS);
                        }

                        @Override
                        public void failure(TwitterException exception) {
                            Toast.makeText(AddingTweetActivity.this, "Nie udało się wysłać tweeta", Toast.LENGTH_SHORT).show();
                            Log.i("Stefan", "Failure update", exception);
                        }
                    });

                } else {
                    Toast.makeText(AddingTweetActivity.this, "Tweet ma powyżej 280 znaków", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}