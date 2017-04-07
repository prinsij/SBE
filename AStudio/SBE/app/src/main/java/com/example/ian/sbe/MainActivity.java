package com.example.ian.sbe;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    //*****TWITTER*******************


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

    ProgressDialog pDialog;

    MenuItem login;
    MenuItem logout;
    FloatingActionButton fab;
    TextView output;

    char [][] outputArray;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();

    // Internet Connection detector
    private ConnectionDetector cd;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    //************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        output = (TextView) findViewById(R.id.output);
        // http://stackoverflow.com/questions/6078194/how-to-set-font-width-in-an-android-textview
        output.setTextScaleX(1.5f);
        //output.setTextSize(12f);
        output.setTypeface(Typeface.MONOSPACE);



        login = (MenuItem) navigationView.getMenu().findItem(R.id.nav_login);//(MenuItem) findViewById(R.id.nav_login);
        logout= (MenuItem) navigationView.getMenu().findItem(R.id.nav_logout);;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                takeScreenshot(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        verifyStoragePermissions(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);

        /** This if conditions is tested once is
         * redirected from twitter page. Parse the uri to get oAuth
         * Verifier
         * */
        if (!isTwitterLoggedInAlready()) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);

                    // Shared Preferences
                    SharedPreferences.Editor e = mSharedPreferences.edit();

                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                    e.putString(PREF_KEY_OAUTH_SECRET,
                            accessToken.getTokenSecret());
                    // Store login status - true
                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                    e.commit(); // save changes

                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());
                    login.setVisible(true);
                    logout.setVisible(false);
                    fab.setVisibility(View.GONE);
                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();
               } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        } else {
            login.setVisible(false);
            logout.setVisible(true);
            fab.setVisibility(View.VISIBLE);
        }





        outputArray = new char[17][17];
        //new runStation().execute("test");
        new Thread() {
            @Override
            public void run() {


                try {
                    StationBuilder builder = new StationBuilder();
                    builder.build();

                    // works, just unnecessary rn
                    /*
                    LoadSaveController.serialize("testsave.sbe", getApplicationContext());
                    Log.d("SBE", "before serial: " + Entity.allEntities().size());
                    ArrayList<Entity> serial = LoadSaveController.deserialize("testsave.sbe", getApplicationContext());
                    Log.d("SBE", "serial: " + serial.size());
                    Log.d("SBE", "after serial: " + Entity.allEntities().size());
                    */

                    AtmosphericController atmo = new AtmosphericController();
                    PersonnelController persons = new PersonnelController();
                    while (true) {
                        atmo.mainLoop();
                        persons.mainLoop();
                        for (int y = 0; y < builder.getY(); y++) {
                            StringBuilder str = new StringBuilder();
                            for (int x = 0; x < builder.getX(); x++) {
                                int maxLayer = -1;
                                char maxSymbol = '?';
                                for (Symbol symbol : Entity.getAllComponentsAt(new Coord(x, y), Symbol.class)) {
                                    if (symbol.getLayer() > maxLayer) {
                                        maxLayer = symbol.getLayer();
                                        maxSymbol = symbol.getSymbol();
                                    }
                                }
                                str.append(' ').append(maxSymbol);
                                outputArray[y][x] = maxSymbol;
                            }
                            Log.d("SBE", str.toString());

                        }
                        Log.d("SBE", "map print");
                        try {
                            Log.d("SBE", Entity.getComponentAt(new Coord(3, 3), GasStorage.class).toString());
                            String toPrint= new String();
                            for (int y = 0;y < outputArray.length;y++){
                                String line ="";

                                for (int x = 0; x < outputArray.length;x++){
                                    line+=outputArray[y][x];
                                }
                                toPrint+=line+"\n";
                            }
                            output.setText(toPrint);
                            output.invalidate();
                        } catch (ComponentNotFoundException e) {}
                    }
                } catch (Exception e) {
                    Log.d("SBE", e.getMessage());
                    Log.d("SBE", e.getStackTrace().toString());
                }
            }
        }.start();




    }

    class runStation extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("testing");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
            StationBuilder builder = new StationBuilder();
            builder.build();

            LoadSaveController.serialize("testsave.sbe", getApplicationContext());
            Log.d("SBE", "before serial: " + Entity.allEntities().size());
            ArrayList<Entity> serial = LoadSaveController.deserialize("testsave.sbe", getApplicationContext());
            Log.d("SBE", "serial: " + serial.size());
            Log.d("SBE", "after serial: " + Entity.allEntities().size());

            AtmosphericController atmo = new AtmosphericController();
            PersonnelController persons = new PersonnelController();
            PowerController power = new PowerController();
            while (true) {
                if (atmo.isEnabled())
                    atmo.mainLoop();
                if (persons.isEnabled())
                    persons.mainLoop();
                if (power.isEnabled())
                    power.mainLoop();
                if (Utils.getRand().nextDouble() < 1.0) {
                    for (int y = 0; y < builder.getY(); y++) {
                        StringBuilder str = new StringBuilder();
                        for (int x = 0; x < builder.getX(); x++) {
                            int maxLayer = -1;
                            char maxSymbol = '?';
                            for (Symbol symbol : Entity.getAllComponentsAt(new Coord(x, y), Symbol.class)) {
                                if (symbol.getLayer() > maxLayer) {
                                    maxLayer = symbol.getLayer();
                                    maxSymbol = symbol.getSymbol();
                                }
                            }
                            str.append(' ').append(maxSymbol);
                            outputArray[y][x] = maxSymbol;
                        }
                        Log.d("SBE", str.toString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String toPrint= new String();
                            for (int y = 0;y < outputArray.length;y++){
                                String line ="";

                                for (int x = 0; x < outputArray.length;x++){
                                    line+=outputArray[y][x];
                                }
                                toPrint+=line+"\n";
                            }
                            Log.d("progress",toPrint);
                            output.post(new Runnable() {
                                @Override
                                public void run() {
                                    output.setText("hhhhh");
                                    output.postInvalidate();
                                }

                            });
                        }
                    });
              //      publishProgress((int) ((2 / (float) 3) * 100));



                  //  updateScreen();
                    Log.d("SBE", "map print");
                    try {
                        Log.d("SBE", Entity.getComponentAt(new Coord(3, 3), GasStorage.class).toString());
                    } catch (ComponentNotFoundException e) {}
                    Log.d("SBE", "total draw (-): "+power.totalStationDraw());
                }
            }
        } catch (Exception e) {
            Log.d("SBE", e.getMessage());
            Log.d("SBE", e.getStackTrace().toString());
        }
            return null;
        }

        private class TempView extends SurfaceView{
            Paint paint;
            public TempView(Context context){
                super(context);
                this.paint = new Paint();
                this.paint.setTextSize(40);
            }
            @Override
            protected void onDraw(Canvas canvas){
                super.onDraw(canvas);
                canvas.drawText(Arrays.deepToString(outputArray), 150, 150, this.paint);

            }
        }


        protected void onProgressUpdate(Integer... progress){
            //super.onProgressUpdate(progress);
            final char [][] outputArrayyy = outputArray;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String toPrint= new String();
                    for (int y = 0;y < outputArrayyy.length;y++){
                        String line ="";

                        for (int x = 0; x < outputArrayyy.length;x++){
                            line+=outputArrayyy[y][x];
                        }
                        toPrint+=line+"\n";
                    }
                    Log.d("progress",toPrint);
                    output.post(new Runnable() {
                        @Override
                        public void run() {
                            output.setText("hhhhh");
                            output.invalidate();
                        }

                    });
                }
            });
        }
        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String toPrint= new String();
                    for (int y = 0;y < outputArray.length;y++){
                        String line ="";
                        for (int x = 0; x < outputArray.length;x++){
                            line+=outputArray[y][x];
                        }
                        toPrint+=line+"\n";
                    }
                    output.setText(toPrint);
                    output.invalidate();
                }
            });
        }
    }

    public void updateScreen(){
        Log.d("test", Arrays.deepToString(outputArray));
        String toPrint= new String();
        for (int y = 0;y < outputArray.length;y++){
            String line ="";
            for (int x = 0; x < outputArray.length;x++){
                line+=outputArray[y][x];
            }
            toPrint+=line+"\n";
        }
        output.setText(toPrint);
        output.invalidate();
    }

    public void takeScreenshot(View view){
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        Bitmap myBitmap = v1.getDrawingCache();
        String screenshotFilePath = Environment.getExternalStorageDirectory().toString();
        File file = new File(screenshotFilePath, "screenshot.png");
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            new updateTwitterStatus().execute("My status in game:");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to login twitter
     */
    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();

            try {
                requestToken = twitter
                        .getOAuthRequestToken(TWITTER_CALLBACK_URL);
                login.setVisible(false);
                logout.setVisible(true);
                fab.setVisibility(View.VISIBLE);
                this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(requestToken.getAuthenticationURL())));
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
        login.setVisible(false);
        logout.setVisible(true);
        fab.setVisibility(View.VISIBLE);
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    /**
     * Function to logout from twitter
     * It will just clear the application shared preferences
     * */
    private void logoutFromTwitter() {
        // Clear the shared preferences
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();

        login.setVisible(true);
        logout.setVisible(false);
        fab.setVisibility(View.GONE);
    }

    /**
     * Function to update status
     */
    class updateTwitterStatus extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Updating to twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            Log.d("Tweet Text", "> " + args[0]);
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

                // Access Token
                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                // Access Token Secret
                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                String statusMessage = "Watch out this interesting offer I came across today";
                //File file = new File("main/res/drawable/image.jpg");
                // File file2 = new File(new BufferedInputStream(getResources().openRawResource(R.drawable.image)))
                // SubSystem.out.println(file.getPath());

                StatusUpdate statusPost = new StatusUpdate(status);
//                if (imagePath != null){
//                    statusPost.setMedia(imagePath); // set the image to be uploaded here.
//                }

                //**********************
                //   Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.fail);
                // String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File file = new File(Environment.getExternalStorageDirectory().toString(), "screenshot.png");
                statusPost.setMedia(file);

                //**********************


                // Update status
                twitter4j.Status response = twitter.updateStatus(statusPost);

                Log.d("Status", "> " + response.getText());
            } catch (TwitterException e) {
                // Error in updating status
                Log.d("Twitter Update Error", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog and show
         * the data in UI Always use runOnUiThread(new Runnable()) to update UI
         * from background thread, otherwise you will get error
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Status tweeted successfully", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

    }
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_power) {

        } else if (id == R.id.nav_atmosphere) {

        } else if (id == R.id.nav_personnel) {

        } else if (id == R.id.nav_login) {
            loginToTwitter();
        } else if (id == R.id.nav_logout) {
            logoutFromTwitter();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
