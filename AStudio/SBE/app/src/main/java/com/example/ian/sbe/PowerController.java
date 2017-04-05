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

    public void toggleAirlock(Coord where) {
        for (Entity entity : Entity.getAt(where)) {
            toggleAirlock(entity);
        }
    }

    private void toggleAirlock(Entity entity) {
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
            GasStorage gas = entity.getComponent(GasStorage.class);
            gas.setActive(!gas.isActive());
        } catch (ComponentNotFoundException e) {}
    }
}
