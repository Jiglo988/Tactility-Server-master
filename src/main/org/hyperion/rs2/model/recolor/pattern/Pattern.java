package org.hyperion.rs2.model.recolor.pattern;

import org.hyperion.rs2.model.color.Color;

public class Pattern {

    public enum Type{
        FILL, REPLACE, CHECKERED, SOLID
    }

    private final Type type;

    protected Pattern(final Type type){
        this.type = type;
    }

    public Type getType(){
        return type;
    }

    public String toString(){
        return type.toString();
    }

    public static Pattern create(final Type type, final int... colors){
        switch(type){
            case FILL:
            case SOLID:
                return new SingleColorPattern(type, colors[0]);
            case REPLACE:
            case CHECKERED:
                return new DualColorPattern(type, colors[0], colors[1]);
            default:
                return null;
        }
    }

    public static Pattern parse(final String line){
        final int i = line.indexOf(' ');
        final Type type = Type.valueOf(line.substring(0, i).trim().toUpperCase());
        final String[] colorParts = line.substring(i+1).split(" ");
        final int[] colors = new int[colorParts.length];
        for(int x = 0; x < colors.length; x++){
            final String part = colorParts[x].trim();
            try{
                colors[x] = Integer.parseInt(part);
            }catch(Exception ex){
                final Color c = Color.byName(part);
                if(c == null)
                    return null;
                colors[x] = c.color;
            }
        }
        return create(type, colors);
    }
}
