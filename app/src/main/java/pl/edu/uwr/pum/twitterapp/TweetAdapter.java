package pl.edu.uwr.pum.twitterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public void setTweetList(List<Tweet> tweetList){
        this.tweetList = tweetList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView usernameTextView, tweetTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            tweetTextView = itemView.findViewById(R.id.tweetContentTextView);
        }
    }
}
