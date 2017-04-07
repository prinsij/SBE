package com.example.ian.sbe;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Steven on 05/04/2017.
 */

public class MenuView {

    ScrollView sv;
    int currentState;
    Slider [] sliders;
    Context context;

    public MenuView(Context context){
        this.context = context;
        this.sv = new ScrollView(context);
        this.setMenus(1);
    }

    public void test() {
        TextView tt = new TextView(context);
        tt.setText("HASHDH");
        tt.setTextSize(100);
        //this.sv.setBackgroundResource(res.drawable.spaceimage);
    }

    public void setMenus(int changeState) {

        currentState = changeState;
        //this.sv = new ScrollView(context);
        this.sv.removeAllViews();

        if (this.currentState == 0) {
            this.sliders = new Slider[MenuData.ATMOSHPERE_HEADINGS.length];
            for (int i = 0; i < MenuData.ATMOSHPERE_HEADINGS.length; i++) {
                this.sliders[i] = new Slider(context, MenuData.ATMOSHPERE_HEADINGS[i], MenuData.ATMOSHPERE_RANGES[i][0], MenuData.ATMOSHPERE_RANGES[i]);
            }
        } else if (this.currentState == 1) {
            this.sliders = new Slider[MenuData.PERSONELL_HEADINGS.length];
            for (int i = 0; i < MenuData.PERSONELL_HEADINGS.length; i++) {
                this.sliders[i] = new Slider(context, MenuData.PERSONELL_HEADINGS[i], MenuData.PERSONELL_RANGES[i][0], MenuData.PERSONELL_RANGES[i]);
            }
        } else {
            this.sliders = new Slider[MenuData.POWER_HEADINGS.length];
            for (int i = 0; i < MenuData.POWER_HEADINGS.length; i++) {
                this.sliders[i] = new Slider(context, MenuData.POWER_HEADINGS[i], MenuData.POWER_RANGES[i][0], MenuData.POWER_RANGES[i]);
            }
        }


        LinearLayout li = new LinearLayout(context);
        li.setOrientation(LinearLayout.VERTICAL);

        MenuChooser  mc = new MenuChooser(context, this);
        li.addView(mc);

        for (Slider sl : this.sliders) {
            li.addView(sl.ll);
        }
        this.sv.addView(li);
        this.sv.invalidate();;
    }
}
