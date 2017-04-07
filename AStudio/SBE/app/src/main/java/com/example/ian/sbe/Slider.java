package com.example.ian.sbe;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Steven on 04/04/2017.
 */
public class Slider {

    String sliderDes;
    LinearLayout ll;
    SeekBar seekbar;
    TextView valueView;

    Context context;

    int curValue;
    int [] range;

    public Slider(Context context, String sliderDes, int curValue, int [] range){
        this.sliderDes = sliderDes;
        this.context = context;
        this.curValue = curValue;
        this.range = range;

        this.createUI();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createUI(){

        this.ll = new LinearLayout(this.context);
        this.ll.setOrientation(LinearLayout.VERTICAL);

        this.seekbar = new SeekBar(this.context);
        this.seekbar.setMax(this.range[1]);
        seekbar.setProgress(this.curValue);
        seekbar.setVisibility(View.VISIBLE);
        LayoutParams lp = new LayoutParams(1200,200);
        seekbar.setLayoutParams(lp);
        this.valueView = new TextView(this.context);
        this.valueView.setText(Integer.toString(this.curValue));
        this.valueView.setTextSize(30);

        TextView temp = new TextView(context);
        temp.setText(this.sliderDes);
        temp.setTextSize(30);

        LinearLayout hl = new LinearLayout(context);
        hl.addView(this.seekbar);
        hl.addView(this.valueView);

        this.ll.addView(temp);
        this.ll.addView(hl);

        this.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeCurValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private boolean checkValidNewValue(int newValue){
        if(newValue >= this.range[0] && newValue <= this.range[1]){
            return true;
        }
        return false;
    }

    public boolean changeCurValue(int newValue){
        if(this.checkValidNewValue(newValue)) {
            this.curValue = newValue;
            this.valueView.setText(Integer.toString(this.curValue));
            return true;
        }
        return false;
    }

}
