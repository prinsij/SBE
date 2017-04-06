package com.example.ian.sbe;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ian on 2017-04-01.
 */

public class AtmosphericController extends System {
    public void mainLoop() {
        for (int i=0; i < Settings.getGasTickRate(); i++) {
            Log.d("SBE", "atmo loop");
            for (Entity ent : Entity.getAllWith(GasStorage.class)) {
                try {
                    //Log.d("SBE", "atmo entity" + ent);
                    GasStorage gas = ent.getComponent(GasStorage.class);
                    if (!gas.isActive()) continue;
                    List<Coord> cardinal = Arrays.asList(Coord.CARDINAL);
                    Collections.shuffle(cardinal);
                    //Log.d("SBE", "cardinal:"+cardinal.toString());
                    for (Coord coord : cardinal) {
                        for (Entity ent2 : Entity.getAt(coord.add(ent.getCoord()))) {
                            try {
                                GasStorage gas2 = ent2.getComponent(GasStorage.class);
                                if (!gas2.isActive()) continue;
                                //Log.d("SBE", "swap" + ent.getCoord() + ent2.getCoord());
                                for (GasStorage.GAS gastype : GasStorage.GAS.values()) {
                                    int swap = (gas2.getAmount(gastype) - gas.getAmount(gastype)) / 2;
                                    gas.setAmount(gastype, gas.getAmount(gastype) + swap);
                                    gas2.setAmount(gastype, gas2.getAmount(gastype) - swap);
                                }
                            } catch (ComponentNotFoundException e) {}
                        }
                    }
                } catch (ComponentNotFoundException e) {}
            }
            for (Entity entity : Entity.getAllWith(GasTransformer.class)) {
                try {
                    GasTransformer transformer = entity.getComponent(GasTransformer.class);
                    GasStorage storage = Entity.getComponentAt(entity.getCoord(), GasStorage.class);
                    transformer.transform(storage);
                } catch (ComponentNotFoundException e) {
                }
            }
        }
        Log.d("SBE", "atmo complete");
    }

    public void breachHull(Coord where) {
        for (Entity entity : Entity.getAt(where)) {
            try {
                entity.getComponent(Terrain.class);
                Entity.deleteEntity(entity);
            } catch (ComponentNotFoundException e) {}
        }
        StationBuilder.buildSpace(where);
    }

    public void addToxin(Coord where) {
        try {
            GasStorage gas = Entity.getComponentAt(where, GasStorage.class);
            gas.clear();
            gas.setAmount(GasStorage.GAS.TOXIN, 100);
        } catch (ComponentNotFoundException e) {}
    }
}
