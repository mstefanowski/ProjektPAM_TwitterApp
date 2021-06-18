package com.twitter.sdk.android.core;

import pl.edu.uwr.pum.twitterapp.PostAccountService;

public class TwitterUtils {

    public static PostAccountService getPostAccountService(TwitterApiClient twitterApiClient) {
        return twitterApiClient.getService(PostAccountService.class);
    }
}
