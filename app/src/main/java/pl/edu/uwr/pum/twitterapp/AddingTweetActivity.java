package pl.edu.uwr.pum.twitterapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddingTweetActivity extends AppCompatActivity {

    EditText addTweetEditText;
    Button addTweetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_tweet);

        addTweetButton = findViewById(R.id.addTweetButton);
        addTweetEditText = findViewById(R.id.AddTweetEditText);
    }
}