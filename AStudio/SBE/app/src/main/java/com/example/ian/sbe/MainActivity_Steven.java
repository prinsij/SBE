package com.example.steven.threea04menus;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/*

    MainActivity will contain 5 radio buttons
        - Atmoshpere
        - Personell
        - Power
        - Exit
        - Save

 */
public class MainActivity extends Activity {

    static int inval = 0;
    MenuView mv;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        this.mv = new MenuView(this);
        this.setContentView(this.mv.sv);
    }



}

