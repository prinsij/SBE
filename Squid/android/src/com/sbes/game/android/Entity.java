package com.sbes.game.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Entity implements Comparable<Entity> {
    private int id = 0;
    private Coord coord;
    private static Map<Coord, Set<Entity>> entities = new HashMap<>();
    private static int currId = 0;
    private List<Component> components = new ArrayList<>();

    public static Iterable<Entity> allEntities() {
        Set<Entity> result = new TreeSet<>();
        for (Set<Entity> set : entities.values()) {
            result.addAll(set);
        }
        return result;
    }

    public Entity(Coord coord) {
        this.id = currId;
        this.coord = coord;
        currId += 1;
        if (entities.containsKey(coord)) {
            entities.get(coord).add(this);
        } else {
            Set<Entity> set = new HashSet<>();
            set.add(this);
            entities.put(coord, set);
        }
    }

    public Coord getCoord() {
        return this.coord;
    }

    public static Iterable<Entity> getAt(Coord coord) {
        Set<Entity> result = entities.get(coord);
        if (result != null) {
            return result;
        }
        return new HashSet<Entity>();
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
        for (Set<Entity> set : entities.values()) {
            for (Entity entity : set) {
                try {
                    entity.getComponent(component);
                    result.add(entity);
                } catch (ComponentNotFoundException e) {
                }
            }
        }
        return result;
    }

    @Override
    public int compareTo(Entity other) {
        if (this.id < other.id) {
            return -1;
        } else if (this.id > other.id) {
            return 1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
