package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public class Coord {
    private final int x, y;

    Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return this.x;
    }

    int getY() {
        return this.y;
    }

    Coord add(Coord other) {
        return new Coord(this.x + other.x, this.y + other.y);
    }
}
