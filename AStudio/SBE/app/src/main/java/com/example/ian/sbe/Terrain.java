package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-02.
 */

public class Terrain extends Component {
    private boolean isPassable;

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean isPassable) {
        this.isPassable = isPassable;
    }

    Terrain(boolean isPassable) {
        this.isPassable = isPassable;
    }
}
