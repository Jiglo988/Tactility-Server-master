package org.hyperion.rs2.model.joshyachievementsv2.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Tasks{

    private final Map<Integer, Task> map;

    public int threshold;

    public Tasks(final Collection<Task> collection){
        map = new TreeMap<>();

        collection.forEach(this::add);
    }

    public Tasks(){
        this(new ArrayList<>());
    }

    public Task get(final int id){
        return map.get(id);
    }

    public int size(){
        return map.size();
    }

    public Collection<Task> values(){
        return map.values();
    }

    public Stream<Task> stream(){
        return values().stream();
    }

    public void add(final Task task){
        map.put(task.id, task);
        threshold += task.threshold;
    }
}
