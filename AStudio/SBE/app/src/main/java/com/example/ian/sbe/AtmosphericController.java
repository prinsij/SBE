package com.example.ian.sbe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Ian on 2017-04-01.
 */

public class AtmosphericController extends System {
    public void mainLoop() {
        while (Utils.getRand().nextDouble() >= Settings.getPercolateHaltChance()) {
            for (Entity ent : Entity.allEntities()) {
                try {
                    GasStorage gas = ent.getComponent(GasStorage.class);
                    List<Coord> cardinal = Arrays.asList(Coord.CARDINAL);
                    Collections.shuffle(cardinal);
                    for (Coord coord : cardinal) {
                        for (Entity ent2 : Entity.getAt(coord)) {
                            try {
                                GasStorage gas2 = ent2.getComponent(GasStorage.class);
                                for (GasStorage.GAS gastype : GasStorage.GAS.values()) {
                                    int swap = (gas2.getAmount(gastype) - gas.getAmount(gastype)) / 2;
                                    gas.setAmount(gastype, gas.getAmount(gastype) + swap);
                                    gas2.setAmount(gastype, gas2.getAmount(gastype) - swap);
                                }
                            } catch (ComponentNotFoundException e) {
                            }
                        }
                    }
                } catch (ComponentNotFoundException e) {
                }
                try {
                    GasTransformer transformer = ent.getComponent(GasTransformer.class);
                    GasStorage storage = null;
                    for (Entity ent2 : Entity.getAt(ent.getCoord())) {
                        try {
                            storage = ent2.getComponent(GasStorage.class);
                        } catch (ComponentNotFoundException e) {
                        }
                    }
                    if (storage != null) {
                        boolean success = transformer.transform(storage);
                        if (!success) {
                            Health hp = ent.getComponent(Health.class);
                            hp.subtract(transformer.getFailurePenalty());
                        }
                    }
                } catch (ComponentNotFoundException e) {
                }
            }
        }
    }
}
