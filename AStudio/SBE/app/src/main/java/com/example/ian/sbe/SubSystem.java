package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public abstract class SubSystem {
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // allows swapping of system at runtime
    private boolean enabled = true;
    public abstract void mainLoop();
}
