package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-02.
 */

public class Terrain extends Component {
    private final boolean isPassable;

    public boolean isPassable() {
        return isPassable;
    }

    Terrain(boolean isPassable) {
        this.isPassable = isPassable;
    }
}
