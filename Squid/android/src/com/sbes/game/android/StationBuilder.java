package com.sbes.game.android;

/**
 * Created by Ian on 2017-04-01.
 */

public class StationBuilder {
    private final int X, Y;
    private final int startingCrew = 5;

    public StationBuilder(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public StationBuilder() {
        this.X = 30;
        this.Y = 30;
    }

    public void build() {
        for (int x=0; x < X; x++) {
            for (int y=0; y < Y; y++) {
                new Entity(new Coord(x, y))
                        .add(new GasStorage())
                        .add(new Symbol('.'));
            }
        }
        for (int q=0; q < startingCrew; q++) {
            new Entity(new Coord(0+q, 0))
                    .add(new Health(10))
                    .add(new Symbol('@'));
        }
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

}
