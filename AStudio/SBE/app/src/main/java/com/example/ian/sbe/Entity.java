package com.example.ian.sbe;

import java.util.ArrayList;
import java.util.Map;

public class Entity {
    private int id = 0;
    private Coord coord;
    private static Map<Coord, Entity> entities;
    private static int currId = 0;
    private ArrayList<Component> components;

    public static Iterable<Entity> allEntities() {
        return entities.values();
    }

    public Entity(Coord coord) {
        this.id = currId;
        this.coord = coord;
        currId += 1;
        entities.put(coord, this);
    }

    public Coord getCoord() {
        return this.coord;
    }

    public Entity add(Component component) {
        this.components.add(component);
        return this;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public int getId() {
        return this.id;
    }

    public <T extends Component> T getComponent(Class<T> component) throws ComponentNotFoundException {
        for (Component comp : this.components) {
            try {
                return component.cast(comp);
            } catch (ClassCastException e) {}
        }
        throw new ComponentNotFoundException();
    }

    public static <T extends Component> Iterable<Entity> getAllWith(Class<T> component) {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity entity : entities.values()) {
            try {
                entity.getComponent(component);
                result.add(entity);
            } catch (ComponentNotFoundException e) {}
        }
        return result;
    }
}
