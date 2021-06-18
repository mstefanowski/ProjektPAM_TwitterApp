package pl.edu.uwr.pum.twitterapp;

import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostAccountService {


    @POST("/1.1/account/update_profile.json")
    Call<User> postChangedSettings(@Query("name") String name,
                                   @Query("url") String url,
                                   @Query("location") String location,
                                   @Query("description") String description,
                                   @Query("profile_link_color") String profile_link_color,
                                   @Query("include_entities") String include_entities,
                                   @Query("skip_status") String skip_status);
}