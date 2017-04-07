package com.example.ian.sbe;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Ian on 2017-04-02.
 */

public class PersonnelController extends SubSystem {

    public void mainLoop() {
        // handle damage
        for (Entity entity : Entity.getAllWith(Health.class)) {
            try {
                Health health = entity.getComponent(Health.class);
                health.subtract(health.checkForDamage(Entity.getComponentAt(entity.getCoord(), GasStorage.class)));
                // death
                if (health.getHp() <= 0) {
                    Entity.deleteEntity(entity);
                }
            } catch (ComponentNotFoundException e) {}
        }
        // handle tasks
        for (Entity entity : Entity.getAllWith(Task.class)) {
            try {
                Task task = entity.getComponent(Task.class);
                if (task instanceof WayPoint) {
                    // move along path if possible
                    try {
                        Coord where = ((WayPoint) task).getWhere();
                        List<Coord> path = pathBetween(entity.getCoord(), where);
                        Coord nextPos = path.get(0);
                        Log.d("SBE", "wp from " + entity.getCoord().toString() + " to " + where.toString());
                        entity.setCoord(nextPos);
                        Log.d("SBE", "nextpos: " + nextPos.toString());
                        if (nextPos.equals(where)) {
                            entity.removeComponent(task);
                        }
                    } catch (Exception e) {
                        Log.d("SBE", e.getMessage().toString());
                    }
                }
            } catch (ComponentNotFoundException e) {}
        }
    }

    // stimulus
    public void assignWayPoint(Entity person, Coord where) {
        person.removeAllComponents(Task.class);
        person.add(new WayPoint(where));
    }

    // simple BFS path-finding
    private List<Coord> pathBetween(Coord from, Coord to) throws Exception {
        Queue<Coord> queue = new LinkedList<Coord>();
        Map<Coord, List<Coord>> map = new HashMap<>();
        map.put(from, new ArrayList<Coord>());
        queue.add(from);

        while (!queue.isEmpty()) {
            Coord current = queue.remove();
            if (current.equals(to)) {
                return map.get(current);
            }
            for (Coord card : Coord.CARDINAL) {
                Coord next = current.add(card);
                if (map.containsKey(next)) {
                    continue;
                }
                try {
                    if (!Entity.getComponentAt(next, Terrain.class).isPassable())
                        continue;
                } catch (ComponentNotFoundException e) {
                    continue;
                }
                List<Coord> oldList = map.get(current);
                List<Coord> nextList = new ArrayList<>(oldList.size());
                for (Coord coord : oldList) {
                    nextList.add(coord);
                }
                nextList.add(next);
                map.put(next, nextList);
                queue.add(next);
            }
        }
        throw new Exception("No path found");
    }
}
