package com.example.ian.sbe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by AreebMalik on 2017-04-06.
 */

public class TwitterAPI {
    File imagePath;

    String screenshotFilePath;

    //Areeb's twitter account:
    static String TWITTER_CONSUMER_KEY = "a0wD2AO1umUbNMBglG5UsJvAI";
    static String TWITTER_CONSUMER_SECRET = "LvGyygNUXaKDHlRiyfIzmRdY16kQFWY6UDIqbO8SxqHQ9lY4ip";

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;


    public TwitterAPI() {

    }

    public void uploadToTwitter(String filePath) {
        screenshotFilePath = filePath;
    }
    /**
     * Function to login twitter
     */
//    private void loginToTwitter() {
//        // Check if already logged in
//        if (!isTwitterLoggedInAlready()) {
//            ConfigurationBuilder builder = new ConfigurationBuilder();
//            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//            Configuration configuration = builder.build();
//
//            TwitterFactory factory = new TwitterFactory(configuration);
//            twitter = factory.getInstance();
//
//            try {
//                requestToken = twitter
//                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
//                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
//                        .parse(requestToken.getAuthenticationURL())));
//            } catch (TwitterException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // user already logged into twitter
//            Toast.makeText(getApplicationContext(),
//                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    /**
//     * Check user already logged in your application using twitter Login flag is
//     * fetched from Shared Preferences
//     */
//    private boolean isTwitterLoggedInAlready() {
//        // return twitter login status from Shared Preferences
//        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
//    }
//
//
//
//
//    /**
//     * Function to update status
//     */
//    class updateTwitterStatus extends AsyncTask<String, String, String> {
//
//        /**
//         * Before starting background thread Show Progress Dialog
//         */
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Updating to twitter...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        /**
//         * getting Places JSON
//         */
//        protected String doInBackground(String... args) {
//            Log.d("Tweet Text", "> " + args[0]);
//            String status = args[0];
//            try {
//                ConfigurationBuilder builder = new ConfigurationBuilder();
//                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//
//                // Access Token
//                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
//                // Access Token Secret
//                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
//
//                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
//                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
//
//                String statusMessage = "Watch out this interesting offer I came across today";
//                //File file = new File("main/res/drawable/image.jpg");
//                // File file2 = new File(new BufferedInputStream(getResources().openRawResource(R.drawable.image)))
//                // System.out.println(file.getPath());
//
//                StatusUpdate statusPost = new StatusUpdate(status);
////                if (imagePath != null){
////                    statusPost.setMedia(imagePath); // set the image to be uploaded here.
////                }
//
//                //**********************
//                //   Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.fail);
//                // String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//                File file = new File(Environment.getExternalStorageDirectory().toString(), "screenshot.png");
//                statusPost.setMedia(file);
//
//                //**********************
//
//
//                // Update status
//                twitter4j.Status response = twitter.updateStatus(statusPost);
//
//                Log.d("Status", "> " + response.getText());
//            } catch (TwitterException e) {
//                // Error in updating status
//                Log.d("Twitter Update Error", e.getMessage());
//            }
//            return null;
//        }
//
//        /**
//         * After completing background task Dismiss the progress dialog and show
//         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
//         * from background thread, otherwise you will get error
//         **/
//        protected void onPostExecute(String file_url) {
//            // dismiss the dialog after getting all products
//            pDialog.dismiss();
//            // updating UI from Background Thread
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(),
//                            "Status tweeted successfully", Toast.LENGTH_SHORT)
//                            .show();
//                    // Clearing EditText field
//                    txtUpdate.setText("");
//                }
//            });
//        }
//
//    }
//    /**
//     * Function to logout from twitter
//     * It will just clear the application shared preferences
//     * */
//    private void logoutFromTwitter() {
//        // Clear the shared preferences
//        SharedPreferences.Editor e = mSharedPreferences.edit();
//        e.remove(PREF_KEY_OAUTH_TOKEN);
//        e.remove(PREF_KEY_OAUTH_SECRET);
//        e.remove(PREF_KEY_TWITTER_LOGIN);
//        e.commit();
//
//        // After this take the appropriate action
//        // I am showing the hiding/showing buttons again
//        // You might not needed this code
//        btnLogoutTwitter.setVisibility(View.GONE);
//        btnUpdateStatus.setVisibility(View.GONE);
//        txtUpdate.setVisibility(View.GONE);
//        lblUpdate.setVisibility(View.GONE);
//        lblUserName.setText("");
//        lblUserName.setVisibility(View.GONE);
//
//        btnLoginTwitter.setVisibility(View.VISIBLE);
//    }
//
//    protected void onResume() {
//        super.onResume();
//    }
//}
}