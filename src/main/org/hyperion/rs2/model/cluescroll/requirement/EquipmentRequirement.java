package org.hyperion.rs2.model.cluescroll.requirement;

import org.hyperion.rs2.model.Item;
import org.hyperion.rs2.model.Player;
import org.hyperion.rs2.model.cluescroll.util.ClueScrollUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EquipmentRequirement extends Requirement{

    private int slot;
    private int itemId;

    public EquipmentRequirement(final int slot, final int itemId){
        super(Type.EQUIPMENT);
        this.slot = slot;
        this.itemId = itemId;
    }

    public int getSlot(){
        return slot;
    }

    public void setSlot(final int slot){
        this.slot = slot;
    }

    public int getItemId(){
        return itemId;
    }

    public void setItemId(final int itemId){
        this.itemId = itemId;
    }

    public boolean apply(final Player player){
        final Item item = player.getEquipment().get(slot);
        return item != null && item.getId() == itemId;
    }

    protected void append(final Document doc, final Element root){
        root.appendChild(ClueScrollUtils.createElement(doc, "slot", slot));
        root.appendChild(ClueScrollUtils.createElement(doc, "itemId", itemId));
    }

    public String toString(){
        return String.format("%s: Slot[%d] = %d", super.toString(), slot, itemId);
    }

    public static EquipmentRequirement parse(final Element element){
        final int slot = ClueScrollUtils.getInteger(element, "slot");
        final int itemId = ClueScrollUtils.getInteger(element, "itemId");
        return new EquipmentRequirement(slot, itemId);
    }
}
