package org.hyperion.rs2.model.content.jge.entry.progress;

import org.hyperion.rs2.model.content.jge.entry.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Administrator on 9/24/2015.
 */
public class ProgressManager {

    public final Entry entry;
    public final List<Progress> list;

    public ProgressManager(final Entry entry){
        this.entry = entry;

        list = new ArrayList<>();
    }

    public Progress last(){
        return list.get(list.size()-1);
    }

    public ProgressManager copy(){
        final ProgressManager copy = new ProgressManager(entry);
        stream().map(Progress::copy)
                .forEach(copy::add);
        return copy;
    }

    public Stream<Progress> stream(){
        return list.stream();
    }

    public double quantityPercent(){
        return totalQuantity() * 100d / entry.itemQuantity;
    }

    public int totalPrice(){
        switch(entry.type){
            case BUYING:
                return stream()
                        .mapToInt(p -> p.unitPrice)
                        .sum() * totalQuantity();
            case SELLING:
                return totalQuantity() * entry.unitPrice;
            default:
                return 0;
        }

    }

    public int totalQuantity(){
        return stream()
                .mapToInt(p -> p.quantity)
                .sum();
    }

    public int remainingQuantity(){
        return entry.itemQuantity - totalQuantity();
    }

    public void add(final String playerName, final int unitPrice, final int quantity){
        add(new Progress(playerName, entry.type.opposite(), unitPrice, quantity));
    }

    public void add(final Progress progress){
        list.add(progress);
    }

    public boolean completed(){
        return totalQuantity() == entry.itemQuantity;
    }

    public String toSaveString(){
        return stream()
                .map(Progress::toSaveString)
                .collect(Collectors.joining(","));
    }

    public static ProgressManager fromSaveString(final Entry entry, final String progress){
        final ProgressManager manager = new ProgressManager(entry);
        if(progress.isEmpty())
            return manager;
        Stream.of(progress.split(","))
                .map(Progress::fromSaveString)
                .forEach(manager::add);
        return manager;
    }
}
