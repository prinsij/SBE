package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-02.
 */

public class PowerController extends System {
    @Override
    public void mainLoop() {

    }

    private int totalStationDraw() {
        int total = 0;
        for (PowerDraw power : Entity.getAllComponents(PowerDraw.class)) {
            if (power.isOn()) {
                total += power.getAmount();
            }
        }
        return total;
    }
}
