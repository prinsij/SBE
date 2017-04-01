package com.example.ian.sbe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StationBuilder builder = new StationBuilder();
        builder.build();
        AtmosphericController atmo = new AtmosphericController();
        while (true) {
            atmo.mainLoop();
            if (Utils.getRand().nextDouble() < .1) {
                for (int y=0; y < builder.getY(); y++) {
                    StringBuilder str = new StringBuilder();
                    for (int x=0; x < builder.getX(); x++) {
                        char symbol = ' ';
                        for (Entity ent : Entity.getAt(new Coord(x, y))) {
                            try {
                                symbol = ent.getComponent(Symbol.class).getSymbol();
                            } catch (ComponentNotFoundException e) {
                            }
                        }
                        str.append(' ').append(symbol);
                    }
                    Log.d(str.toString());
                }
            }
        }
    }
}
