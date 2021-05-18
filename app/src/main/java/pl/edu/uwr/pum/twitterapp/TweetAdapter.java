package pl.edu.uwr.pum.twitterapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    List<Tweet> tweetList;
    Context context;

    public TweetAdapter(Context context, List<Tweet> tweetList) {
        this.context = context;
        this.tweetList = tweetList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_tweet_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.usernameTextView.setText(tweetList.get(position).user.screenName);
        holder.tweetTextView.setText(tweetList.get(position).text);


        int hearts = tweetList.get(position).favoriteCount;
        int retweets = tweetList.get(position).retweetCount; //bierze followy z retweetowanego posta

        holder.heartsTextView.setText(String.valueOf(hearts));
        holder.retweetsTextView.setText(Integer.toString(retweets));

        String imageUrl = tweetList.get(position).user.profileImageUrlHttps;
        Picasso.get().load(imageUrl).into(holder.avatarImageView);

        holder.favouriteButtonToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(context, "POLAJKOWANE", Toast.LENGTH_SHORT).show();
                    holder.heartsTextView.setText(String.valueOf(hearts + 1));
                    TwitterCore.getInstance().getApiClient().getFavoriteService().create(tweetList.get(position).id, null).enqueue(new Callback<Tweet>() {
                        @Override
                        public void success(Result<Tweet> result) {

                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });

                } else {
                    Toast.makeText(context, "ODLAJKOWANE", Toast.LENGTH_SHORT).show();
                    holder.heartsTextView.setText(String.valueOf(hearts));
                    TwitterCore.getInstance().getApiClient().getFavoriteService().destroy(tweetList.get(position).id, null).enqueue(new Callback<Tweet>() {
                        @Override
                        public void success(Result<Tweet> result) {

                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });
                }
            }
        });

        holder.retweetButtonToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.retweetsTextView.setText(String.valueOf(retweets + 1));
                    TwitterCore.getInstance().getApiClient().getStatusesService().retweet(tweetList.get(position).id, null).enqueue(new Callback<Tweet>() {
                        @Override
                        public void success(Result<Tweet> result) {

                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });

                } else {
                    holder.retweetsTextView.setText(String.valueOf(retweets));
                    TwitterCore.getInstance().getApiClient().getStatusesService().unretweet(tweetList.get(position).id, null).enqueue(new Callback<Tweet>() {
                        @Override
                        public void success(Result<Tweet> result) {

                        }

                        @Override
                        public void failure(TwitterException exception) {

                        }
                    });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public void setTweetList(List<Tweet> tweetList){
        this.tweetList = tweetList;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView usernameTextView, tweetTextView, heartsTextView, retweetsTextView;
        ImageView avatarImageView;
        ToggleButton favouriteButtonToggle, retweetButtonToggle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            favouriteButtonToggle = itemView.findViewById(R.id.favouriteToggleButton);
            retweetButtonToggle = itemView.findViewById(R.id.retweetToggleButton);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            tweetTextView = itemView.findViewById(R.id.tweetContentTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            heartsTextView = itemView.findViewById(R.id.heartsTextView);
            retweetsTextView = itemView.findViewById(R.id.retweetsTextView);
        }
    }
}
