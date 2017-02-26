package org.hyperion.rs2.model.punishment;

import org.hyperion.rs2.model.Player;

public class Combination {

    private final Target target;
    private final Type type;

    public Combination(final Target target, final Type type){
        this.target = target;
        this.type = type;
    }

    public void apply(final Player player){
        getTarget().apply(player, getType());
    }

    public boolean isApplied(final Player player){
        return getTarget().isApplied(player, getType());
    }

    public void unapply(final Player player){
        getTarget().unapply(player, getType());
    }

    public Target getTarget(){
        return target;
    }

    public Type getType(){
        return type;
    }

    public boolean equals(final Object o){
        if(o == null)
            return false;
        if(o == this)
            return true;
        if(!(o instanceof Combination))
            return false;
        final Combination c = (Combination) o;
        return c.target == target && c.type == type;
    }

    public int hashCode(){
        return getTarget().hashCode() * getType().hashCode();
    }

    public String toString(){
        return String.format("%s %s", target, type);
    }

    public static Combination of(final Target target, final Type type){
        return new Combination(target, type);
    }
}
