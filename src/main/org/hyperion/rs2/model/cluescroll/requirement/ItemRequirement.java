package org.hyperion.rs2.model.cluescroll.requirement;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ItemRequirement extends Requirement{

    private int id;
    private int amount;

    public ItemRequirement(final int id, final int amount){
        super(Type.ITEM);
        this.id = id;
        this.amount = amount;
    }

    public int getId(){
        return id;
    }

    public void setId(final int id){
        this.id = id;
    }

    public int getAmount(){
        return amount;
    }

    public void setAmount(final int amount){
        this.amount = amount;
    }

    public boolean apply(final Player player){
        final Item item = player.getInventory().getById(id);
        return item != null && item.getCount() >= amount;
    }

    protected void append(final Document doc, final Element root){
        root.appendChild(ClueScrollUtils.createElement(doc, "id", id));
        root.appendChild(ClueScrollUtils.createElement(doc, "amount", amount));
    }

    public String toString(){
        return String.format("%s: ID = %d x %,d", super.toString(), id, amount);
    }

    public static ItemRequirement parse(final Element element){
        final int id = ClueScrollUtils.getInteger(element, "id");
        final int amount = ClueScrollUtils.getInteger(element, "amount");
        return new ItemRequirement(id, amount);
    }
}
