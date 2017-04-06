package com.example.steven.threea04menus;

import android.content.Context;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by Steven on 04/04/2017.
 */
public class MenuChooser extends LinearLayout{

    private static final int BUTTON_WIDTH = 500;
    private static final int BUTTON_HEIGHT = 300;

    MenuView mv;
    Context context;
    Button atmosphereButton;
    Button personellButton;
    Button powerButton;
    Button saveButton;
    Button exitButton;

    int curChosen; // 0 ~ 4


    public MenuChooser(Context context, MenuView mv){
        super(context);
        this.mv = mv;
        this.context = context;
        curChosen = mv.currentState;
        this.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(2000, 260);
        this.setLayoutParams(lp);

        this.atmosphereButton = new Button(this.context);
        this.atmosphereButton.setText("Atmoshpere");

        this.personellButton = new Button(this.context);
        this.personellButton.setText("Personell");

        this.powerButton = new Button(this.context);
        this.powerButton.setText("Power");

        this.saveButton = new Button(this.context);
        this.saveButton.setText("Save");

        this.exitButton = new Button(this.context);
        this.exitButton.setText("Exit");



        final Button [] buttonArray = new Button[5];
        buttonArray[0] = this.atmosphereButton;
        buttonArray[1] = this.personellButton;
        buttonArray[2] = this.powerButton;
        buttonArray[3] = this.saveButton;
        buttonArray[4] = this.exitButton;

        for(int i=0; i<buttonArray.length; i++){
            Button bb = buttonArray[i];
            bb.setPadding(0,0,0,0);
            bb.setLeft(0);
            bb.setMinimumWidth(MenuChooser.BUTTON_WIDTH);
            bb.setMinimumHeight(MenuChooser.BUTTON_HEIGHT);

            if(curChosen == i){
                bb.setBackgroundColor(Color.GRAY);
            }

            final int j = i;
            bb.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    curChosen = j;
                    buttonClicked(buttonArray, j);
                }
            });
        }


        this.addView(this.atmosphereButton);
        this.addView(this.personellButton);
        this.addView(this.powerButton);
        this.addView(this.saveButton);
        this.addView(this.exitButton);
    }

    public void buttonClicked(Button [] bb, int k){
        for(int i=0; i<bb.length; i++){
            bb[i].setBackgroundColor(Color.WHITE);
        }
        bb[k].setBackgroundColor(Color.GRAY);

        if(k == 4){
            // exit button has been clicekd
        } else {
            System.out.println("THIS HAS RUN");
            //this.mv.test();;
            this.mv.setMenus(k);
        }
    }

    // save and apass values
    public void saveAndPassValues(int a, int b, int c){

    }

}
