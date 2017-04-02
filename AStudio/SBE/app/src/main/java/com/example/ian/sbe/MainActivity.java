package com.example.ian.sbe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            StationBuilder builder = new StationBuilder();
            builder.build();
            AtmosphericController atmo = new AtmosphericController();
            PersonnelController persons = new PersonnelController();
            Log.d("SBE", "app initialized");
            while (true) {
                atmo.mainLoop();
                persons.mainLoop();
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
                        }
                        Log.d("SBE", str.toString());
                    }
                    Log.d("SBE", "map print");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SBE", e.getMessage());
            Log.d("SBE", e.getStackTrace().toString());
        }
    }
}
