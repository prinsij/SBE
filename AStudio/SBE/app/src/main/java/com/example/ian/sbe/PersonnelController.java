package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-02.
 */

public class PersonnelController extends System {

    @Override
    public void mainLoop() {
        for (Entity entity : Entity.getAllWith(Health.class)) {
            Health health = null;
            try {
                health = entity.getComponent(Health.class);
                health.subtract(health.checkForDamage(Entity.getComponentAt(entity.getCoord(), GasStorage.class)));
            } catch (ComponentNotFoundException e) {}
        }
    }
}
