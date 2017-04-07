package com.example.ian.sbe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Steven on 05/04/2017.
 */
public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle saved){
        super.onCreate(saved);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout hl = new LinearLayout(this);
        hl.setOrientation(LinearLayout.HORIZONTAL);

        TextView tv = new TextView(this);
        tv.setText("SpaceBase Ephemeris");
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(60);

        Button newButton = new Button(this);
        Button loadButton = new Button(this);
        hl.addView(newButton);
        hl.addView(loadButton);

        ll.addView(tv);
        ll.addView(hl);
        //ll.setBackgroundResource(R.drawable.spaceimage);

        this.setContentView(ll);
    }

}
