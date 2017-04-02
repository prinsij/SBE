package com.sbes.game.android;

/**
 * Created by Ian on 2017-04-01.
 */

public class Coord {
    private final int x, y;
    public static final Coord[] ORTHO = {new Coord(0,1),new Coord(1,0),new Coord(1,1),
                                         new Coord(0,-1),new Coord(-1,0),new Coord(-1,-1),
                                         new Coord(1,-1),new Coord(-1,1)};
    public static final Coord[] CARDINAL = {new Coord(1,0),new Coord(0,1),
                                            new Coord(-1,0),new Coord(0,-1)};

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

    public String toString() {
        return "(" + Integer.toString(this.x) + "," + Integer.toString(this.y) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coord coord = (Coord) o;

        if (x != coord.x) return false;
        return y == coord.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
