package org.hyperion.rs2.model.cluescroll.requirement;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.Position;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LocationRequirement extends Requirement{

    private int x;
    private int y;
    private int z;

    public LocationRequirement(final int x, final int y, final int z){
        super(Type.LOCATION);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX(){
        return x;
    }

    public void setX(final int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(final int y){
        this.y = y;
    }

    public int getZ(){
        return z;
    }

    public void setZ(final int z){
        this.z = z;
    }

    public boolean apply(final Player player){
        return player.getPosition().equals(Position.create(x, y, z != -1 ? z : player.getPosition().getZ()));
    }

    protected void append(final Document doc, final Element root){
        root.appendChild(ClueScrollUtils.createElement(doc, "x", x));
        root.appendChild(ClueScrollUtils.createElement(doc, "y", y));
        root.appendChild(ClueScrollUtils.createElement(doc, "z", z));
    }

    public String toString(){
        return String.format("%s: %d,%d,%d", super.toString(), x, y, z);
    }

    public static LocationRequirement parse(final Element element){
        final int x = ClueScrollUtils.getInteger(element, "x");
        final int y = ClueScrollUtils.getInteger(element, "y");
        final int z = ClueScrollUtils.getInteger(element, "z");
        return new LocationRequirement(x, y, z);
    }
}
