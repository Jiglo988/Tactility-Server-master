package org.hyperion.rs2.model.recolor.pattern;

public class SingleColorPattern extends Pattern{

    private final int color;

    protected SingleColorPattern(final Type type, final int color){
        super(type);
        this.color = color;
    }

    public int getColor(){
        return color;
    }

    public String toString(){
        return String.format("%s %d", super.toString(), color);
    }
}
