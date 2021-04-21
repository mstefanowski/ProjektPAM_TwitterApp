package pl.edu.uwr.pum.twitterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.usernameTextView.setText(tweetList.get(position).user.screenName);
        holder.tweetTextView.setText(tweetList.get(position).text);

        String hearts = tweetList.get(position).favoriteCount.toString();
        int retweets = tweetList.get(position).retweetCount; //bierze followy z retweetowanego posta

        holder.heartsTextView.setText(hearts);
        holder.retweetsTextView.setText(Integer.toString(retweets));

        String imageUrl = tweetList.get(position).user.profileImageUrlHttps;
        Picasso.get().load(imageUrl).into(holder.avatarImageView);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            tweetTextView = itemView.findViewById(R.id.tweetContentTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            heartsTextView = itemView.findViewById(R.id.heartsTextView);
            retweetsTextView = itemView.findViewById(R.id.retweetsTextView);
        }
    }
}
