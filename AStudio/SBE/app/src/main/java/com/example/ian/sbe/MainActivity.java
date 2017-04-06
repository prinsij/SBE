package com.example.ian.sbe;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //*****TWITTER*******************
    TwitterAPI twitterAPI = new TwitterAPI();
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //takeScreenshot();
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

//        try {
//            StationBuilder builder = new StationBuilder();
//            builder.build();
//
//            LoadSaveController.serialize("testsave.sbe", getApplicationContext());
//            Log.d("SBE", "before serial: " + Entity.allEntities().size());
//            ArrayList<Entity> serial = LoadSaveController.deserialize("testsave.sbe", getApplicationContext());
//            Log.d("SBE", "serial: " + serial.size());
//            Log.d("SBE", "after serial: " + Entity.allEntities().size());
//
//            AtmosphericController atmo = new AtmosphericController();
//            PersonnelController persons = new PersonnelController();
//            while (true) {
//                atmo.mainLoop();
//                persons.mainLoop();
//                if (Utils.getRand().nextDouble() < 1.0) {
//                    for (int y = 0; y < builder.getY(); y++) {
//                        StringBuilder str = new StringBuilder();
//                        for (int x = 0; x < builder.getX(); x++) {
//                            int maxLayer = -1;
//                            char maxSymbol = '?';
//                            for (Symbol symbol : Entity.getAllComponentsAt(new Coord(x, y), Symbol.class)) {
//                                if (symbol.getLayer() > maxLayer) {
//                                    maxLayer = symbol.getLayer();
//                                    maxSymbol = symbol.getSymbol();
//                                }
//                            }
//                            str.append(' ').append(maxSymbol);
//                        }
//                        Log.d("SBE", str.toString());
//                    }
//                    Log.d("SBE", "map print");
//                    try {
//                        Log.d("SBE", Entity.getComponentAt(new Coord(3, 3), GasStorage.class).toString());
//                    } catch (ComponentNotFoundException e) {}
//                }
//            }
//        } catch (Exception e) {
//            Log.d("SBE", e.getMessage());
//            Log.d("SBE", e.getStackTrace().toString());
//        }
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
            twitterAPI.uploadToTwitter(screenshotFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
