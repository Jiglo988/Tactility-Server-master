package org.hyperion.rs2.model.joshyachievementsv2.constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hyperion.rs2.model.Player;

public class Constraints{

    public final List<Constraint> list;

    public Constraints(final Collection<Constraint> collection){
        list = new ArrayList<>(collection);
    }

    public Constraints(){
        this(new ArrayList<>());
    }

    public void add(final Constraint constraint){
        list.add(constraint);
    }

    public boolean constrained(final Player player){
        return list.isEmpty() || list.stream()
                .allMatch(c -> c.constrained(player));
    }
}
