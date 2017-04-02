package com.example.ian.sbe;


/**
 * Created by Ian on 2017-04-02.
 */

public class WayPoint extends Task {
    private final Coord where;

    public WayPoint(Coord where) {
        this.where = where;
    }

    public Coord getWhere() {
        return where;
    }
}
