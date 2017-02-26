package org.hyperion.rs2.model.cluescroll.reward;

import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Reward {

    public enum Type{
        ITEM{
            public ItemReward parse(final Element element){
                return ItemReward.parse(element);
            }

            public ItemReward createDefault(){
                return new ItemReward(1, 1, 1, 1);
            }
        },
        POINTS{
            public PointsReward parse(final Element element){
                return PointsReward.parse(element);
            }

            public PointsReward createDefault(){
                return new PointsReward(PointsReward.Type.PK_POINTS, 1, 1, 1);
            }
        },
        EXPERIENCE{
            public ExperienceReward parse(final Element element){
                return ExperienceReward.parse(element);
            }

            public ExperienceReward createDefault(){
                return new ExperienceReward(0, 1, 1, 1);
            }
        };

        public abstract Reward parse(final Element element);

        public abstract Reward createDefault();
    }

    private final Type type;
    private int minAmount;
    private int maxAmount;
    private int chance;

    protected Reward(final Type type, final int minAmount, final int maxAmount, final int chance){
        this.type = type;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.chance = chance;
    }

    public Type getType(){
        return type;
    }

    public int getMinAmount(){
        return minAmount;
    }

    public void setMinAmount(final int minAmount){
        this.minAmount = minAmount;
    }

    public int getMaxAmount(){
        return maxAmount;
    }

    public void setMaxAmount(final int maxAmount){
        this.maxAmount = maxAmount;
    }

    public double getChance(){
        return chance;
    }

    public void setChance(final int chance){
        this.chance = chance;
    }

    public boolean apply(final Player player){
        return give(player, ClueScrollUtils.rand(minAmount, maxAmount));
    }

    public boolean apply(final Player player, int index){return give(player, ClueScrollUtils.rand(minAmount, maxAmount), index);
    }

    public boolean canGet(){
        return ClueScrollUtils.isChance(chance);
    }

    public Element toElement(final Document doc){
        final Element element = doc.createElement("reward");
        element.setAttribute("type", type.name());
        append(doc, element);
        element.appendChild(ClueScrollUtils.createElement(doc, "minAmount", minAmount));
        element.appendChild(ClueScrollUtils.createElement(doc, "maxAmount", maxAmount));
        element.appendChild(ClueScrollUtils.createElement(doc, "chance", chance));
        return element;
    }

    public Element toRareElement(final Document doc){
        final Element element = doc.createElement("rareReward");
        element.setAttribute("type", type.name());
        append(doc, element);
        element.appendChild(ClueScrollUtils.createElement(doc, "minAmount", minAmount));
        element.appendChild(ClueScrollUtils.createElement(doc, "maxAmount", maxAmount));
        element.appendChild(ClueScrollUtils.createElement(doc, "chance", chance));
        return element;
    }

    public String toString(){
        return String.format("%s [%,d, %,d] @ %d%%", type.name(), minAmount, maxAmount, chance);
    }

    protected abstract void append(final Document doc, final Element root);

    protected abstract boolean give(final Player player, final int amount);

    protected boolean give(final Player player, final int amount, int index) {
        return false;
    };
}
