package pl.edu.uwr.pum.twitterapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Media;
import com.twitter.sdk.android.core.models.Tweet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AddingTweetActivity extends AppCompatActivity {

    EditText addTweetEditText;
    Button addTweetButton, addPictureButton;
    static final int UPDATE_TWEETS = 100;
    static final int SELECT_PHOTO = 1;
    Uri uri;
    String photoMediaId;
    Bitmap pictureBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_tweet);

        addTweetButton = findViewById(R.id.addTweetButton);
        addPictureButton = findViewById(R.id.addPictureButton);

        addTweetEditText = findViewById(R.id.AddTweetEditText);


        addTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetText = addTweetEditText.getText().toString();

                if (tweetText.length() < 280) {
                    if (pictureBitmap != null) {
                        Log.i("Stefan", "PICTURE BITMAP != null");
                        byte[] byteArray = toByteArray(pictureBitmap);
                        RequestBody body = RequestBody.create(byteArray, MediaType.parse("application/octet-stream"));
                        TwitterCore.getInstance().getApiClient().getMediaService().upload(body, null, null).enqueue(new Callback<Media>() {
                            @Override
                            public void success(Result<Media> result) {
                                photoMediaId = result.data.mediaIdString;
                                TwitterCore.getInstance().getApiClient().getStatusesService().update(tweetText,
                                        null, null, null,
                                        null, null, null,
                                        null, photoMediaId).enqueue(new Callback<Tweet>() {
                                    @Override
                                    public void success(Result<Tweet> result) {

                                        finish();
                                    }

                                    @Override
                                    public void failure(TwitterException exception) {
                                        Toast.makeText(AddingTweetActivity.this, "Nie udało się wysłać tweeta", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Log.i("Stefan", "Media upload failure", exception);
                            }
                        });
                    } else {
                        TwitterCore.getInstance().getApiClient().getStatusesService().update(tweetText,
                                null, null, null,
                                null, null, null,
                                null, null).enqueue(new Callback<Tweet>() {
                            @Override
                            public void success(Result<Tweet> result) {

                                finish();
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                Toast.makeText(AddingTweetActivity.this, "Nie udało się wysłać tweeta", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                } else {
                    Toast.makeText(AddingTweetActivity.this, "Tweet ma powyżej 280 znaków", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                pictureBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    byte[] toByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();;
        return byteArray;
    }
}