package com.example.ian.sbe;

import android.util.Log;

/**
 * Created by Ian on 2017-04-02.
 */

public class PowerController extends SubSystem {
    @Override
    public void mainLoop() {
        int draw = totalStationDraw();
        if (draw > 0) {
            // turn off drawing devices semi-randomly until power within means
            for (PowerDraw power : Entity.getAllComponents(PowerDraw.class)) {
                if (draw <= 0) {
                    break;
                }
                if (power.getAmount() > 0) {
                    draw -= power.getAmount();
                    power.toggle(false);
                }
            }
        }
        for (Entity entity : Entity.getAllWith(OpenCloseActivation.class)) {
            // randomly toggle doors for the demo
            if (Utils.getRand().nextDouble() < .1) {
                toggleAirlock(entity);
            }
        }
    }

    // stimulus
    public void toggleDevice(Coord where) {
        for (PowerDraw power : Entity.getAllComponentsAt(where, PowerDraw.class)) {
            power.toggle(!power.isOn());
        }
    }

    public int totalStationDraw() {
        int total = 0;
        for (PowerDraw power : Entity.getAllComponents(PowerDraw.class)) {
            if (power.isOn()) {
                total += power.getAmount();
            }
        }
        return total;
    }

    // stimulus
    public void toggleAirlock(Coord where) {
        for (Entity entity : Entity.getAt(where)) {
            toggleAirlock(entity);
        }
    }

    private void toggleAirlock(Entity entity) {
        Log.d("SBE", "toggle airlock: " + entity.getCoord());
        try {
            PowerDraw power = entity.getComponent(PowerDraw.class);
            if (!power.isOn()) {
                return;
            }
        } catch (ComponentNotFoundException e) {}
        // bail out if there's a health-having entity so we don't crush them
        if (Entity.getAllComponentsAt(entity.getCoord(), Health.class).size() > 0) {
            return;
        }
        try {
            entity.getComponent(OpenCloseActivation.class);
            Terrain terrain = entity.getComponent(Terrain.class);
            terrain.setPassable(!terrain.isPassable());
            try {
                if (terrain.isPassable()) {
                    entity.getComponent(Symbol.class).setSymbol(OpenCloseActivation.open);
                } else {
                    entity.getComponent(Symbol.class).setSymbol(OpenCloseActivation.closed);
                }
            } catch (ComponentNotFoundException e) {}
        } catch (ComponentNotFoundException e) {}
        try {
            GasStorage gas = entity.getComponent(GasStorage.class);
            gas.setActive(!gas.isActive());
        } catch (ComponentNotFoundException e) {}

    }
}
