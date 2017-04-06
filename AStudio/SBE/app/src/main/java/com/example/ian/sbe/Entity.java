package com.example.ian.sbe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Entity implements Comparable<Entity>, Serializable {
    private static final long serialVersionUID = 1;
    private int id = 0;
    private Coord coord;
    private static Map<Coord, Set<Entity>> entities = new HashMap<>();
    private static int currId = 0;
    private List<Component> components = new ArrayList<>();

    public static void clearEntities() {
        entities.clear();
    }

    public static void register(Entity entity) {
        Coord coord = entity.getCoord();
        if (entities.containsKey(coord)) {
            entities.get(coord).add(entity);
        } else {
            Set<Entity> set = new HashSet<>();
            set.add(entity);
            entities.put(coord, set);
        }
    }

    public static ArrayList<Entity> allEntities() {
        ArrayList<Entity> result = new ArrayList<>();
        for (Set<Entity> set : entities.values()) {
            for (Entity entity : set) {
                result.add(entity);
            }
        }
        return result;
    }

    public Entity(Coord coord) {
        this.id = currId;
        this.coord = coord;
        currId += 1;
        register(this);
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

    public void removeComponent(Component component) {
        this.components.remove(component);
    }

    public static void deleteEntity(Entity entity) {
        entities.get(entity.getCoord()).remove(entity);
    }

    public static <T extends Component> T getComponentAt(Coord coord, Class<T> component) throws ComponentNotFoundException {
        for (Entity entity : getAt(coord)) {
            for (Component c : entity.components) {
                try {
                    return entity.getComponent(component);
                } catch (Exception e) {}
            }
        }
        throw new ComponentNotFoundException();
    }

    public static <T extends Component> Iterable<T> getAllComponentsAt(Coord coord, Class<T> component) {
        ArrayList<T> result = new ArrayList<T>();
        for (Entity entity : getAt(coord)) {
            try {
                result.add(entity.getComponent(component));
            } catch (ComponentNotFoundException e) {}
        }
        return result;
    }

    public Entity add(Component component) {
        this.components.add(component);
        return this;
    }

    public void setCoord(Coord coord) {
        Set<Entity> atCurrentTile = entities.get(this.getCoord());
        atCurrentTile.remove(this);
        if (atCurrentTile.isEmpty()) {
            entities.remove(this.getCoord());
        }
        this.coord = coord;
        if (entities.containsKey(coord)) {
            entities.get(coord).add(this);
        } else {
            Set<Entity> newSet = new TreeSet<Entity>();
            newSet.add(this);
            entities.put(coord, newSet);
        }
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

    public static <T extends Component> Iterable<T> getAllComponents(Class<T> component) {
        ArrayList<T> result = new ArrayList<>();
        for (Set<Entity> set : entities.values()) {
            for (Entity entity : set) {
                try {
                    result.add(entity.getComponent(component));
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

    public String toString() {
        return "Entity:" + Integer.toString(this.id);
    }
}
