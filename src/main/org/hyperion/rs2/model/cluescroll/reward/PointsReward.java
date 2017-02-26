package org.hyperion.rs2.model.cluescroll.reward;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PointsReward extends Reward{

    public enum Type{
        PK_POINTS("Pk Points"){
            public void apply(final Player player, final int amount){
                player.getPoints().setPkPoints(player.getPoints().getPkPoints() + amount);
            }
        },
        DONATOR_POINTS("Donator Points"){
            public void apply(final Player player, final int amount){
                player.getPoints().setDonatorPoints(player.getPoints().getDonatorPoints() + amount);
            }
        };

        private final String name;

        private Type(final String name){
            this.name = name;
        }

        public abstract void apply(final Player player, final int amount);

        public String toString(){
            return name;
        }
    }

    private Type type;

    public PointsReward(final Type type, final int minAmount, final int maxAmount, final int chance){
        super(Reward.Type.POINTS, minAmount, maxAmount, chance);
        this.type = type;
    }

    public Type getPointsType(){
        return type;
    }

    public void setPointsType(final Type type){
        this.type = type;
    }

    protected boolean give(final Player player, final int amount){
        type.apply(player, amount);
        player.sendf("@blu@You receive %,d %s", amount, type.toString().toLowerCase());
        return true;
    }

    protected void append(final Document doc, final Element root){
        root.appendChild(ClueScrollUtils.createElement(doc, "type", type.name()));
    }

    public String toString(){
        return String.format("%s: %s", super.toString(), type.name());
    }

    public static PointsReward parse(final Element element){
        final Type type = Type.valueOf(ClueScrollUtils.getString(element, "type"));
        final int minAmount = ClueScrollUtils.getInteger(element, "minAmount");
        final int maxAmount = ClueScrollUtils.getInteger(element, "maxAmount");
        final int chance = ClueScrollUtils.getInteger(element, "chance");
        return new PointsReward(type, minAmount, maxAmount, chance);
    }
}
