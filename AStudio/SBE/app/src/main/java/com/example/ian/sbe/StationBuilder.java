package com.example.ian.sbe;

/**
 * Created by Ian on 2017-04-01.
 */

public class StationBuilder {
    private final int X, Y;
    private final int startingCrew = 3;
    private final int roomX = 3, roomY = 3, roomWidth = 3, roomHeight = 3, separation = 1;


    public StationBuilder(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public StationBuilder() {
        this.X = 30;
        this.Y = 30;
    }

    private void buildWall(Coord coord) {
        new Entity(coord)
                .add(new Symbol('#'))
                .add(new Terrain(false));
    }

    private void buildSpace(Coord coord) {
        new Entity(coord)
                .add(new GasStorage())
                .add(new Symbol(' '))
                .add(new Terrain(false))
                .add(new VacuumTransformer());
    }

    private void buildFloor(Coord coord) {
        new Entity(coord)
                .add(new GasStorage(GasStorage.GAS.OXYGEN))
                .add(new Symbol('.'))
                .add(new Terrain(true));
    }

    private void buildAirlock(Coord coord) {
        new Entity(coord)
                .add(new GasStorage(false))
                .add(new Symbol('%'))
                .add(new Terrain(true));
    }

    private void placePerson(Coord coord) {
        new Entity(coord)
                .add(new Health())
                .add(new Symbol('@', 1))
                .add(new WayPoint(coord.add(new Coord(0, 2))))
                .add(new BreathTransformer());
    }

    public void build() {
        // build rooms
        for (int x=0; x < roomX; x++) {
            for (int y=0; y < roomY; y++) {
                Coord roomCoord = new Coord(x*(roomWidth+2+separation), y*(roomHeight+2+separation));
                for(int x2=0; x2 <= roomWidth+1; x2++) {
                    for (int y2=0; y2 <= roomHeight+1; y2++) {
                        Coord position = roomCoord.add(new Coord(x2, y2));
                        if ((x2 != 0 && y2 != 0 && x2 != roomWidth+1 && y2 != roomHeight+1)) {
                            buildFloor(position);
                        } else if (    (y2 == roomHeight/2+1 && x2 == roomWidth+1  && x != roomX-1) // right door
                                    || (x2 == roomWidth/2+1  && y2 == roomHeight+1 && y != roomY-1) // bottom door
                                    || (y2 == roomHeight/2+1 && x2 == 0 && x != 0) // left door
                                    || (x2 == roomWidth/2+1  && y2 == 0 && y != 0)) { // top door
                            buildAirlock(position);
                        } else {
                            buildWall(position);
                        }
                    }
                }
                // horizontal tunnel
                if (x != roomX -1) {
                    buildWall(roomCoord.add(new Coord(roomWidth+2, roomHeight/2)));
                    buildFloor(roomCoord.add(new Coord(roomWidth+2, roomHeight/2+1)));
                    buildWall(roomCoord.add(new Coord(roomWidth+2, roomHeight/2+2)));
                }
                if (y != roomY -1) {
                    buildWall(roomCoord.add(new Coord(roomWidth/2, roomHeight+2)));
                    buildFloor(roomCoord.add(new Coord(roomWidth/2+1, roomHeight+2)));
                    buildWall(roomCoord.add(new Coord(roomWidth/2+2, roomHeight+2)));
                }
            }
        }
        //fill in the empty space
        for (int x=0; x < X; x++) {
            for (int y=0; y < Y; y++) {
                Coord position = new Coord(x, y);
                try {
                    Entity.getComponentAt(position, Terrain.class);
                } catch (ComponentNotFoundException e) {
                    buildSpace(position);
                }
            }
        }
        // place crew
        for (int q=0; q < startingCrew; q++) {
            placePerson(new Coord(q+1, 1));
        }
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

}
