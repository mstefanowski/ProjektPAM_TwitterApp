package pl.edu.uwr.pum.twitterapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {

    private TwitterLoginButton loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                requestEmailAddress(getApplicationContext(), result.data);

                Intent intent = new Intent(getApplicationContext(), TweetListActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(TwitterException exception) {
                // Upon error, show a toast message indicating that authorization request failed.
                Toast.makeText(getApplicationContext(), exception.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void requestEmailAddress(final Context context, TwitterSession session) {
        new TwitterAuthClient().requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                Toast.makeText(context, "gituwa dziala", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the saveSession button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
//
//    private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
//
//    //private TwitterExample() {}
//
//    @SuppressWarnings("PMD.SystemPrintln")
//    public static void main() throws IOException, InterruptedException, ExecutionException {
//        final OAuth10aService service = new ServiceBuilder("your client id")
//                .apiSecret("your client secret")
//                .build(TwitterApi.instance());
//        final Scanner in = new Scanner(System.in);
//
//        System.out.println("=== Twitter's OAuth Workflow ===");
//        System.out.println();
//
//        // Obtain the Request Token
//        System.out.println("Fetching the Request Token...");
//        final OAuth1RequestToken requestToken = service.getRequestToken();
//        System.out.println("Got the Request Token!");
//        System.out.println();
//
//        System.out.println("Now go and authorize ScribeJava here:");
//        System.out.println(service.getAuthorizationUrl(requestToken));
//        System.out.println("And paste the verifier here");
//        System.out.print(">>");
//        final String oauthVerifier = in.nextLine();
//        System.out.println();
//
//        // Trade the Request Token and Verifier for the Access Token
//        System.out.println("Trading the Request Token for an Access Token...");
//        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
//        System.out.println("Got the Access Token!");
//        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
//        System.out.println();
//
//        // Now let's go and ask for a protected resource!
//        System.out.println("Now we're going to access a protected resource...");
//        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
//        service.signRequest(accessToken, request);
//        Response response = service.execute(request);
//        System.out.println("Got it! Lets see what we found...");
//        System.out.println();
//        System.out.println(response.getBody());
//        System.out.println();
//        System.out.println("That's it man! Go and build something awesome with ScribeJava! :)");
//        Log.i("Stefan", "Test");
//    }
}