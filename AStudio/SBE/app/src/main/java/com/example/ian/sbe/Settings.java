package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public class Settings {
    // number of atmospheric ticks per frame
    private final int gasTickRate = 1;
    // regulator targets and max amount it change a gas in a tick
    private int idealPressure = 100, idealo2 = 21, idealc02 = 6, idealToxin = 0;
    private int regulatorChange = 3;

    private static Settings singleton = new Settings();

    public static Settings getSingleton() {
        return singleton;
    }

    public static void setSingleton(Settings settings) {
        singleton = settings;
    }

    private Settings() {}

    public int getGasTickRate() {
        return gasTickRate;
    }

    public int getIdealPressure() {
        return idealPressure;
    }

    public int getIdealo2() {
        return idealo2;
    }

    public int getIdealc02() {
        return idealc02;
    }

    public int getIdealToxin() {
        return idealToxin;
    }

    public int getRegulatorChange() {
        return regulatorChange;
    }
}
